package ir.rayan.data.sql;

import com.sun.javafx.binding.StringFormatter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mj.rahmati on 12/4/2019.
 */
public class SQLManager {
    private SQLModelManager modelManager;
    private static SQLManager sqlManager;
    private JdbcDaoSupport daoSupport;

    private Object getValue(String key, HttpServletRequest request, Map<String, Object> paramMap) {
        Object obj = null;
        if(request != null) {
            obj = request.getParameter(key);
            if (obj == null)
                obj = request.getAttribute(key);
            if (obj == null)
                obj = request.getSession().getAttribute(key);
            request.getSession().setAttribute(key, obj);
        }
        if(paramMap != null && obj == null)
            obj = paramMap.get(key);
//        if(obj == null)
//            return "";
        return obj;
    }

    private Object getValue(String key, String type, HttpServletRequest request, Map<String, Object> paramMap) {
        Object obj = null;
        if(request != null) {
            obj = request.getParameter(key);
            if (obj == null)
                obj = request.getAttribute(key);
            if (obj == null)
                obj = request.getSession().getAttribute(key);
            request.getSession().setAttribute(key, obj);
        }
        if(paramMap != null && obj == null)
            obj = paramMap.get(key);
        if(obj == null) {
            if(type.equals("string"))
                return "";
            else
                return null;
        }
        return obj;
    }

    private SQLManager(SQLModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public static SQLManager createSQLManager(SQLModelManager sqlModelManager) {
        if(sqlManager == null)
            sqlManager = new SQLManager(sqlModelManager);
        return sqlManager;
    }

    public static SQLManager getSQLManager() {
        if(sqlManager != null)
            return sqlManager;
        return null;
    }

    public SQLModelManager getSQLModelManager() {
        return modelManager;
    }

    public String createQuery(String sourceName, HttpServletRequest request, Map<String, Object> paramMap) {
        if(modelManager.hasSelectDef(sourceName))
            return createSelectQuery(sourceName, request, paramMap);
        else if(modelManager.hasInsertDef(sourceName))
            return createInsertQuery(sourceName, request, paramMap);
        else if(modelManager.hasUpdateDef(sourceName))
            return createUpdateQuery(sourceName, request, paramMap);
        return null;
    }

    public String createSelectQuery(String sourceName, HttpServletRequest request, Map<String, Object> paramMap) {
        SelectDef selectDef = modelManager.getSelectDef(sourceName);

        StringBuffer wheresBuffer = new StringBuffer("");
        String lastConjuction = null;
        if(selectDef.whereParts == null) {

        } else {
            for (Map.Entry<String[], SQLWhereDef> wherePart : selectDef.whereParts) {
                StringBuffer whereBuffer = new StringBuffer("");
                for (int i = 0; i < wherePart.getKey().length - 1; i++) {
                    if (i % 2 == 0)
                        whereBuffer.append(wherePart.getKey()[i]);
                    else {
                        String paramName = wherePart.getKey()[i];

                        if (paramName != null && !paramName.isEmpty()) {
                            Object paramValue = getValue(selectDef.paramMap.get(paramName).key, request, paramMap);
                            if ((paramValue == null || ((String)paramValue).isEmpty()) && !wherePart.getValue().force)
                                whereBuffer = new StringBuffer("@");
                            else {
                                if (paramValue instanceof List) {
                                    List list = (List) paramValue;
                                    for (Object obj : list)
                                        whereBuffer.append(obj.toString() + ", ");
                                    whereBuffer.deleteCharAt(whereBuffer.length() - 2);
                                } else {
                                    whereBuffer.append(paramValue);
                                }
                            }
                        } else {
                            whereBuffer.append("");
                        }
                    }
                }
                if(whereBuffer.charAt(0) != '@') {
                    lastConjuction = " " + wherePart.getKey()[wherePart.getKey().length - 1] + " ";
                    whereBuffer.append(lastConjuction);
                    wheresBuffer.append(whereBuffer);
                }
            }
        }
        if(wheresBuffer.length() > 0)
            wheresBuffer = new StringBuffer(" where ").append(wheresBuffer);
        if(lastConjuction != null && !lastConjuction.isEmpty())
            wheresBuffer.delete(wheresBuffer.length() - lastConjuction.length(), wheresBuffer.length()).append(" ");
        StringBuffer fromBuffer = new StringBuffer();
        String from = selectDef.fromString;
        if(!from.isEmpty()) {
            for (SelectSourceDef selectSourceDef : selectDef.sources) {
                if(from.contains(" " + selectSourceDef.alias + " ")) {
                    if(selectSourceDef.type != null && selectSourceDef.type.equalsIgnoreCase("source"))
                        from = from.replace(" " + selectSourceDef.alias + " ", " (" + this.createSelectQuery(selectSourceDef.name, request, paramMap) + ") " + selectSourceDef.alias + " ");
                    else {
                        from = from.replace(" " + selectSourceDef.alias + " ", " " + selectSourceDef.name + " " + selectSourceDef.alias + " ");
                    }
                }

            }
            fromBuffer.append(from);
        }

        StringBuffer rightQueryString = new StringBuffer();
        if(selectDef.orderParts != null && !selectDef.orderParts.isEmpty()) {
            rightQueryString.append(" order by ");
            for (Map.Entry<Map.Entry<String, Boolean>, Map.Entry<String, Boolean>> orderPart : selectDef.orderParts) {
                String by = null;
                if(orderPart.getKey().getValue()) {
                    Object paramValue = getValue(selectDef.paramMap.get(orderPart.getKey().getKey()).key, request, paramMap);
                    if(paramValue == null)
                        continue;
                    by = (String) paramValue;
                } else {
                    by = (String) orderPart.getKey().getKey();
                }
                String order = null;
                if(orderPart.getValue().getValue()) {
                    Object paramValue = getValue(selectDef.paramMap.get(orderPart.getValue().getKey()).key, request, paramMap);
                    if(paramValue == null)
                        continue;
                    order = (String) paramValue;
                } else {
                    order = (String) orderPart.getValue().getKey();
                }
                rightQueryString.append(by + " " + order + ", ");
            }
            if(rightQueryString.toString().equalsIgnoreCase(" order by "))
                rightQueryString = new StringBuffer("");
            else
                rightQueryString.deleteCharAt(rightQueryString.length() - 2);
        }

        if(selectDef.groups != null && !selectDef.groups.isEmpty()) {
            rightQueryString.append(" group by ");
            for (String group : selectDef.groups)
                rightQueryString.append(group).append(", ");
            rightQueryString.deleteCharAt(rightQueryString.length() - 2);
        }

        return selectDef.leftQueryString.concat(fromBuffer.toString()).concat(wheresBuffer.toString()).concat(rightQueryString.toString());
    }

    public String createUpdateQuery(String sourceName, HttpServletRequest request, Map<String, Object> paramMap) {
        UpdateDef updateDef = modelManager.getUpdateDef(sourceName);

        StringBuffer columnBuffer = new StringBuffer("");
        for(UpdateColumnDef columnDef : updateDef.columns) {
            Pattern pattern = Pattern.compile("\\$(.*?)\\$");
            String statement = columnDef.statement;
            if (statement.contains("$")) {
                Matcher matcher = pattern.matcher(statement);
                while (matcher.find()) {
                    String group = matcher.group();
                    SQLParamDef sqlParamDef = updateDef.paramMap.get(group.substring(1, group.length() - 1));
                    String[] split = statement.split(Pattern.quote(group));
                    StringBuffer part = new StringBuffer(split[0]).append(getValue(sqlParamDef.key, sqlParamDef.type, request, paramMap))/*.append(split[1])*/;
                    columnBuffer.append(part);
                    statement = "";
                    for(int i = 1; i < split.length; i++)
                        statement = statement.concat(split[i]);
                }
                columnBuffer.append(statement).append(", ");
            } else {
                columnBuffer.append(columnDef.statement).append(", ");
            }
        }
        if(!updateDef.columns.isEmpty())
            columnBuffer.deleteCharAt(columnBuffer.length() - 2);

        StringBuffer wheresBuffer = new StringBuffer("");
        String lastConjuction = null;
        if(updateDef.whereParts != null) {
            for (Map.Entry<String[], SQLWhereDef> wherePart : updateDef.whereParts) {
                StringBuffer whereBuffer = new StringBuffer("");
                for (int i = 0; i < wherePart.getKey().length - 1; i++) {
                    if (i % 2 == 0)
                        whereBuffer.append(wherePart.getKey()[i]);
                    else {
                        String paramName = wherePart.getKey()[i];

                        if (paramName != null && !paramName.isEmpty()) {
                            Object paramValue = getValue(updateDef.paramMap.get(paramName).key, request, paramMap);
                            if ((paramValue == null || ((String)paramValue).isEmpty()) && !wherePart.getValue().force)
                                whereBuffer = new StringBuffer("@");
                            else {
                                if (paramValue instanceof List) {
                                    List list = (List) paramValue;
                                    for (Object obj : list)
                                        whereBuffer.append(obj.toString() + ", ");
                                    whereBuffer.deleteCharAt(whereBuffer.length() - 2);
                                } else {
                                    whereBuffer.append(paramValue);
                                }
                            }
                        } else {
                            whereBuffer.append("");
                        }
                    }
                }
                if(whereBuffer.charAt(0) != '@') {
                    lastConjuction = " " + wherePart.getKey()[wherePart.getKey().length - 1] + " ";
                    whereBuffer.append(lastConjuction);
                    wheresBuffer.append(whereBuffer);
                }
            }
        }
        if(wheresBuffer.length() > 0)
            wheresBuffer = new StringBuffer(" where ").append(wheresBuffer);
        if(lastConjuction != null && !lastConjuction.isEmpty())
            wheresBuffer.delete(wheresBuffer.length() - lastConjuction.length(), wheresBuffer.length()).append(" ");

        return updateDef.updateQuery.concat(columnBuffer.toString()).concat(wheresBuffer.toString());
    }

    public String createInsertQuery(String sourceName, HttpServletRequest request, Map<String, Object> paramMap) {
        paramMap = paramMap != null ? paramMap : new LinkedHashMap<>();
        InsertDef insertDef = modelManager.getInsertDef(sourceName);

        StringBuffer columnBuffer = new StringBuffer("(");
        StringBuffer columnValueBuffer = new StringBuffer(" values (");
        Pattern pattern = Pattern.compile("\\$(.*?)\\$");
        Pattern sharpPattern = Pattern.compile("\\#(.*?)\\#");
        for(InsertColumnDef columnDef : insertDef.columns) {
            columnBuffer.append(columnDef.getName()).append(", ");

            String value = columnDef.value;
            if (value.contains("$")) {
                Matcher matcher = pattern.matcher(value);
                while (matcher.find()) {
                    String group = matcher.group();
                    SQLParamDef sqlParamDef = insertDef.paramMap.get(group.substring(1, group.length() - 1));
                    String[] split = value.split(Pattern.quote(group));
                    if(split.length == 0) {
                        StringBuffer part = new StringBuffer();
                        Object value1 = getValue(sqlParamDef.key, sqlParamDef.type, request, paramMap);
                        if(sqlParamDef.type.equals("string"))
                            part.append("'").append(value1).append("'");
                        else
                            part.append(value1);
                        columnValueBuffer.append(part);
                        value = "";
                    } else {
                        StringBuffer part = new StringBuffer(split[0]).append(getValue(sqlParamDef.key, sqlParamDef.type, request, paramMap))/*.append(split[1])*/;
                        columnValueBuffer.append(part);
                        value = "";
                        for(int i = 1; i < split.length; i++)
                            value = value.concat(split[i]);
                    }
                }
                columnValueBuffer.append(value).append(", ");
            } else if(value.contains("#")) {
                Matcher matcher = sharpPattern.matcher(value);
                while (matcher.find()) {
                    String group = matcher.group();
                    String typeName = group.substring(1, group.length() - 1);
                    String[] split = value.split(sharpPattern.quote(group));
                    if(split.length == 0) {
                        StringBuffer part = new StringBuffer();
                        String val = SQLUtilityType.execute(typeName, request);
                        part.append(val);
                        columnValueBuffer.append(part);
                        value = "";
                    } else {
                        StringBuffer part = new StringBuffer(split[0])
                                .append(SQLUtilityType.execute(split[1], request))/*.append(split[1])*/;
                        columnValueBuffer.append(part);
                        value = "";
                        for(int i = 1; i < split.length; i++)
                            value = value.concat(split[i]);
                    }
                }
                columnValueBuffer.append(value).append(", ");
            } else {
                columnValueBuffer.append(columnDef.value).append(", ");
            }
        }
        if(!insertDef.columns.isEmpty()) {
            columnBuffer.deleteCharAt(columnBuffer.length() - 2).append(")");
            columnValueBuffer.deleteCharAt(columnValueBuffer.length() - 2).append(")");
        }

        return insertDef.insertQuery.concat(columnBuffer.toString()).concat(columnValueBuffer.toString());
    }

    public boolean isSelectQuery(String queryName) {
        return modelManager.hasSelectDef(queryName);
    }

    public boolean isPersistDef(String queryName) {
        return modelManager.hasUpdateDef(queryName) || modelManager.hasInsertDef(queryName);
    }

    public void query(String queryName, DataSource dataSource, HttpServletRequest request, Map<String, Object> paramMap, String attributeName)
            throws SQLException, RayanSQLException {
        if(modelManager.hasSelectDef(queryName))
            querySelect(queryName, dataSource, request, paramMap, attributeName);
        else if(modelManager.hasInsertDef(queryName))
            queryInsert(queryName, dataSource, request, paramMap, attributeName);
        else if(modelManager.hasUpdateDef(queryName))
            queryUpdate(queryName, dataSource, request, paramMap, attributeName);
    }

    public void query(String queryName, DataSource dataSource, HttpServletRequest request, Map<String, Object> paramMap)
            throws SQLException, RayanSQLException {
        query(queryName, dataSource, request, paramMap, queryName);
    }

    public void queryUpdate(String queryName, DataSource dataSource, HttpServletRequest request, Map<String, Object> paramMap, String attributeName)
            throws SQLException, RayanSQLException {
        UpdateDef updateDef = modelManager.getUpdateDef(queryName);

        for(SQLSelectAttribute before : updateDef.befores) {
            querySelect(before.queryName, dataSource, request, paramMap, before.attributeName);
        }

        String query = createQuery(queryName, request, paramMap);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);

        for(SQLSelectAttribute after : updateDef.afters) {
            query(after.queryName, dataSource, request, paramMap, after.attributeName);
        }
    }

    public boolean equality(int value, SQLExecuteIf sqlExecuteIf) {
        if(sqlExecuteIf.evaluation.equals("="))
            return value == Integer.valueOf(sqlExecuteIf.target);
        else if(sqlExecuteIf.evaluation.equals("<="))
            return value <= Integer.valueOf(sqlExecuteIf.target);
        else if(sqlExecuteIf.evaluation.equals(">="))
            return value >= Integer.valueOf(sqlExecuteIf.target);
        else if(sqlExecuteIf.evaluation.equals("<"))
            return value < Integer.valueOf(sqlExecuteIf.target);
        else if(sqlExecuteIf.evaluation.equals(">"))
            return  value > Integer.valueOf(sqlExecuteIf.target);
        else if(sqlExecuteIf.evaluation.equals("!="))
            return  value != Integer.valueOf(sqlExecuteIf.target);
        return false;
    }

    public void queryInsert(String queryName, DataSource dataSource, HttpServletRequest request, Map<String, Object> paramMap, String attributeName)
            throws SQLException, RayanSQLException {
        InsertDef insertDef = modelManager.getInsertDef(queryName);

        for(SQLExecuteIf sqlExecuteIf : insertDef.sqlExecuteIfs) {
            if(sqlExecuteIf.queryForInt != null && !sqlExecuteIf.queryForInt.isEmpty()) {
                String selectQuery = createSelectQuery(sqlExecuteIf.queryForInt, request, paramMap);
                int i = selectForInt(selectQuery, dataSource, request, paramMap);
                if(!equality(i, sqlExecuteIf))
                    return;
            }
        }

        for(SQLSelectAttribute before : insertDef.befores) {
            query(before.queryName, dataSource, request, paramMap, before.attributeName);
        }

        String query = createInsertQuery(queryName, request, paramMap);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        String insertQuery = sqlManager.createQuery(queryName, request, null);
        statement.execute(query);

        for(SQLSelectAttribute after : insertDef.afters) {
            query(after.queryName, dataSource, request, paramMap, after.attributeName);
        }
    }

    public int selectForInt(String query, DataSource dataSource, HttpServletRequest request, Map<String, Object> paramMap)
            throws RayanSQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Integer anInt = null;
            if(resultSet.next())
                anInt = resultSet.getInt(1);
            return anInt;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RayanSQLException(StringFormatter.format("select for int {0} not completed", query).toString());
        }
    }

    public void querySelect(String queryName, DataSource dataSource, HttpServletRequest request, Map<String, Object> paramMap, String attributeName)
            throws SQLException, RayanSQLException {
        SelectDef selectDef = modelManager.getSelectDef(queryName);

        for(SQLExecuteIf sqlExecuteIf : selectDef.sqlExecuteIfs) {
            if(sqlExecuteIf.queryForInt != null && !sqlExecuteIf.queryForInt.isEmpty()) {
                String selectQuery = createSelectQuery(sqlExecuteIf.queryForInt, request, paramMap);
                int i = selectForInt(selectQuery, dataSource, request, paramMap);
                if(!equality(i, sqlExecuteIf))
                    return;
            }
        }

        for(SQLSelectAttribute before : selectDef.befores) {
            querySelect(before.queryName, dataSource, request, paramMap, before.attributeName);
        }

        String query = createSelectQuery(queryName, request, paramMap);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if(resultSet != null) {
            if (selectDef.selectType.equals("int")) {
                if (resultSet.next())
                    request.setAttribute(attributeName, resultSet.getInt(1));
            } else if (selectDef.selectType.equals("result-set")) {
                List<Map<String, String>> items = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, String> map = new LinkedHashMap<>();
                    for (SelectColumnDef selectColumnDef : selectDef.columns) {
                        map.put(selectColumnDef.as, resultSet.getString(selectColumnDef.as));
                    }
                    items.add(map);
                }
                request.setAttribute(attributeName, items);
            }/* else if (selectDef.selectType.equals("fill-request")) {
                List<Map<String, String>> items = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, String> map = new LinkedHashMap<>();
                    for (SelectColumnDef selectColumnDef : selectDef.columns) {
                        map.put(selectColumnDef.as, resultSet.getString(selectColumnDef.as));
                        request.setAttribute(selectColumnDef.as, resultSet.getString(selectColumnDef.as));
                    }
                    items.add(map);
                }
                request.setAttribute(attributeName, items);
            }*/

            for(SQLSelectAttribute after : selectDef.afters) {
                query(after.queryName, dataSource, request, paramMap, after.attributeName);
            }
        }
    }
}

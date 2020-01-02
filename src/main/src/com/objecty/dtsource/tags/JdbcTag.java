package com.objecty.dtsource.tags;

import com.objecty.dtsource.Constants;
import com.objecty.dtsource.ProcessingUtil;
import com.objecty.dtsource.RequestDependent;
import com.objecty.dtsource.service.EntityManager;
import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.List;

/**
 * 
 * Implementation of dtsource:jdbc tag.
 * Works with JDBC's implementation of Entity manager.
 * Provides JDBC-source for loading lists.
 * 
 * @author olexiy, white
 *
 */
public class JdbcTag extends BodyTagSupport {

	// Instantiate Logger class
	private static final Logger log = Logger.getLogger(JdbcTag.class);
		
	private static final long serialVersionUID = -667061401249808276L;
	
	private Long pagesize;			// amount of records per page
	private String id;				// name of the table
	private String list;				// name of attribute in request, where we will put loaded collection
	private String sizelist;			// name of attribute in request, where we will put full size of collection
	private String defaultsortName;	// default sort name, string - will be used if no sort mode clicked
	private String table;				// which table to use, to fetch from
	private String alias;				// shortname, alias of the table
	private String query;				// body, globalized, we set it in doAfterBody, but process in doEndTag
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setDefaultsortName(String defaultsortName) {
		this.defaultsortName = defaultsortName;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setList(String list) {
		this.list = list;
	}

	public void setPagesize(Long pagesize) {
		this.pagesize = pagesize;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setSizelist(String sizelist) {
		this.sizelist = sizelist;
	}

	public int doStartTag() throws JspException {
		// do not have too much to do here
		return EVAL_BODY_BUFFERED;
	}
	
	public int doAfterBody() throws JspException {
		BodyContent body = getBodyContent(); 
		this.query = body.getString().trim();		// truncate extra spaces
		return SKIP_BODY; 
	}

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		// we actually are not doing any output
		//JspWriter out = (JspWriter) pageContext.getOut();
			
		// start with getting know about WebApplicationContext
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		if (context == null) {
			throw new JspException("Cannot locate the spring WebApplicationContext. Is it configured in your web.xml?");
		}
		EntityManager em = (EntityManager) context.getBean(Constants.ENTITYMANAGER_JDBC_NAME, EntityManager.class);
		if (em == null) {
			throw new JspException("Cannot locate the EntityManager in the WebApplicationContext, under the name: '" + Constants.ENTITYMANAGER_JDBC_NAME + "'");
		}

// Changed By mohammad
//        if ((this.query.toUpperCase().lastIndexOf(Constants.TAG_ALIAS.toUpperCase()) == -1) ||
//			(this.query.toUpperCase().lastIndexOf(Constants.TAG_SORT.toUpperCase()) == -1) ||
//			(this.query.toUpperCase().lastIndexOf(Constants.TAG_TABLE.toUpperCase()) == -1)) {
//			throw new JspException("Sorry, but query tags: " + Constants.TAG_ALIAS + ", " + Constants.TAG_SORT + " and " + Constants.TAG_TABLE + " are required.");
//		}
    boolean hasAlias = (this.query.toUpperCase().lastIndexOf(Constants.TAG_ALIAS.toUpperCase()) != -1);
    boolean hasSort = (this.query.toUpperCase().lastIndexOf(Constants.TAG_SORT.toUpperCase()) != -1);
    boolean hasTable = (this.query.toUpperCase().lastIndexOf(Constants.TAG_TABLE.toUpperCase()) != -1);
// Changed By mohammad


        // create an instance of our class with methods we need
		RequestDependent rd = new RequestDependent();
		ProcessingUtil pu = new ProcessingUtil();
		
		// here we need to fetch out values which displaytag kindly gives us 
		Long startFrom = rd.getDisplayTagStartNumber(request, this.id, this.pagesize);
		String sortBy = rd.getDisplayTagSortName(request, this.id);
		String sortMode = rd.getDisplayTagSortOrder(request, this.id);
			
		// nowe we will parse sorting/direction if any; and replace tag with subquery

// Changed By mohammad
        if (hasSort) {
//            String fullSort = pu.createSortSubquery(sortBy, sortMode, this.alias, this.defaultsortName);
          //ramin>>
          HttpSession session = request.getSession();
          if (sortBy == null && sortMode == null) {
            sortBy = (String) session.getAttribute(this.id + ".sortBy");
            sortMode = (String) session.getAttribute(this.id + ".sortMode");
          } else {
            session.setAttribute(this.id + ".sortBy", sortBy);                        
            session.setAttribute(this.id + ".sortMode", sortMode);                        
          }
          //ramin<<
            String fullSort = pu.createSortSubquery(sortBy, sortMode, this.alias, this.defaultsortName, hasAlias);
            this.query = this.query.replaceAll(Constants.TAG_SORT, fullSort);
        }

        // final, we refine query changing alias/entity words wherever necessary
// Changed By mohammad
        if (hasAlias)
		    this.query = this.query.replaceAll(Constants.TAG_ALIAS, this.alias);
        if (hasTable)
            this.query = this.query.replaceAll(Constants.TAG_TABLE, this.table);

		// now, let's check if we have filter params setted, and set useCriteria to true or false
		boolean useCriteria = false;

		String filterField = request.getParameter(pu.getDisplayTagParameter(this.id, Constants.PARAMETER_FILTER_FIELD));
		String filterCriteria = request.getParameter(pu.getDisplayTagParameter(this.id, Constants.PARAMETER_FILTER_CRITERIA));
		if ((filterField != null) && (filterCriteria != null)) {
			useCriteria = true;
		} else {
			log.debug("Not using criteria, filterField is [" + filterField + "] and/or filterCriteria is [" + filterCriteria + "]");
		}
		
		String querySize;		// default is empty
		if (useCriteria) {
			// let's create criteria rule basing on parameters fetched from request
			String criteriaRule = pu.createCriteriaWhereSQL(this.alias, filterField, filterCriteria);
			log.debug("We will use criteria, criteria rule is [" + criteriaRule + "]");
			// let's insert criteria rule right away into query
			try {
				this.query = pu.insertCriteriaRule(this.query, criteriaRule);
			} catch (Exception e) {
				throw new JspException("Query processing failed " + e.getMessage());
			}
			// okay, now that's done. next thing is to extract complete WHERE clause for
			// our custom created query which will do complete counting
			// we are sure that it is there, because we just injected it
			// we assume that it starts with WHERE words and stops with ORDER BY words
			String completeWhereClause = pu.getCompleteWhereClause(this.query);
			querySize = pu.createCountQuerySQL(this.table, this.alias, completeWhereClause);
		} else {
			// here we do not care about criteria
			// so we generate default query size-counter query
			// and that's all
			querySize = pu.createCountQuerySQL(this.table, this.alias);
		}

        // debug information just for now
        log.debug("[after criteria] SQL query for counting size: " + querySize);
        log.debug("[after criteria] SQL query for fetching list: " + query);


        /* The search functionality starts */
	
		// now, let's check if we have search params setted, and set useSearch to true or false
		boolean useSearch = false;

		String searchField = request.getParameter(pu.getDisplayTagParameter(this.id, Constants.PARAMETER_SEARCH_FIELD));
		String searchFrom = request.getParameter(pu.getDisplayTagParameter(this.id, Constants.PARAMETER_SEARCH_FROM));
		String searchTo = request.getParameter(pu.getDisplayTagParameter(this.id, Constants.PARAMETER_SEARCH_TO));
		if ((searchField != null) && (searchFrom != null)) {
			useSearch = true;
		} else {
			log.debug("Not using search, searchField is [" + searchField + "] and/or searchFrom is [" + searchFrom + "]");
		}
		
		if (useSearch) {
			// let's create search rule basing on parameters fetched from request
			String searchRule = pu.createSearchWhereSQL(this.alias, searchField, searchFrom, searchTo);
			log.debug("We will use search, search rule is [" + searchRule + "]");
			// let's insert search rule right away into query
			try {
				// We can use insertCriteriaRule because we are making almost the same rule
				this.query = pu.insertCriteriaRule(this.query, searchRule);
			} catch (Exception e) {
				throw new JspException("Query processing failed " + e.getMessage());
			}
			// okay, now that's done. next thing is to extract complete WHERE clause for
			// our custom created query which will do complete counting
			// we are sure that it is there, because we just injected it
			// we assume that it starts with WHERE words and stops with ORDER BY words
			String completeWhereClause = pu.getCompleteWhereClause(this.query);
			querySize = pu.createCountQuerySQL(this.table, this.alias, completeWhereClause);
		} else {
			// here we do not care about criteria
			// so we generate default query size-counter query
			// and that's all
            //x>>
//			querySize = pu.createCountQuerySQL(this.table, this.alias);
            querySize = "select count(*) from ("+query+")";
            //<<
        }

		/* The search functionality ends */
				
		// debug information just for now
		log.debug("[after search] SQL query for counting size: " + querySize);
		log.debug("[after search] SQL query for fetching list: " + query);
		
		// here we get a size of this list
		Long sizelistValue = em.sizeOfListEntities(querySize);

		// Important note:
		// If you have more than one table on the screen requiring exporting, you need to be sure
		// that they have different values in 'list' and 'sizelist' arguments for dt-Source tag. 
		String exportingMode = request.getParameter(TableTagParameters.PARAMETER_EXPORTING);
		String exportingType = request.getParameter(pu.getDisplayTagParameter(this.id, TableTagParameters.PARAMETER_EXPORTTYPE));
		log.debug("Exporting mode is " + exportingMode + ", exporting type for this table is " + exportingType);
		if ((exportingMode != null) && (exportingType != null)) {
			startFrom = (long) 0;
			this.pagesize = sizelistValue;
		}
		log.debug("Preparing list starting with " + startFrom + " for " + this.pagesize + " element(s)");
		
		// now we actually loading everything from Hibernate
		List listValue = em.listEntities(this.query, startFrom, this.pagesize);

		// put everything required for displaytag (and extra) into request scope
        // TODO: displaytag wants size as Integer! fix it. here is temp. workaround.
        int attrPagesize = (this.pagesize == null) ? 0 : Integer.valueOf(this.pagesize.toString());
        int attrSizelistV = (sizelistValue == null) ? 0 : Integer.valueOf(sizelistValue.toString());
        log.debug("this.pagesize = " + this.pagesize + ", so attrPagesize = " + attrPagesize);
        log.debug("sizelistValue = " + sizelistValue + ", so attrSizelistV = " + attrSizelistV);
        request.setAttribute(Constants.ATTR_PAGESIZE, attrPagesize);
		request.setAttribute(Constants.ATTR_TABLEID, this.id);
		request.setAttribute(this.list, listValue);
		request.setAttribute(this.sizelist, attrSizelistV);
        

        return EVAL_PAGE;
	}


}

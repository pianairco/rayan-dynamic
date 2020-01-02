var MENU_ITEMS = [
        ['حسابداري', null, null,
                ['ليست حسابهاي كل', 'generalLedger.do'  ],
                ['لیست حسابهای معین', 'subsidiaryLedger.do']
        ],
        ['دريافت و پرداخت', null, null ,
                ['ليست دريافت ها ', 'receipt.do']
                ],
        ['درخواست', null, null ,
                ['ليست درخواست‌ها', 'order.do?new_search=true'],
                ['ليست درخواست معاملات', null],
                ['دريافت معاملات روز', null],
                ['پردازش خريد و فروش', 'transaction.do?method=list']
                ],
        ['اطلاعات پایه عملیات', null, null ,
                ['ليست مشتريان', 'customer.do?new_search=true'],
                ['اوراق بهادار جديد', null],
                ['ليست اوراق بهادار', 'instrument.do?new_search=true'],
                ['حق تقدم جدید', null],
                ['ليست حق تقدم', null]
                ],
        ['راهبري سيستم', null, null ,
                ['ليست كاربران', 'appUser.do'],
                ['ليست گروههاي كاربري', 'appRole.do']
        ] ,
        ['اطلاعات پایه', null, null,
                ['پارامترها','appParam.do'],
                ['صنايع', 'industry.do?new_search=true'],
                ['ليست شعب', 'branch.do?new_search=true'],
                ['ليست كارگزاري‌ها', 'brokerage.do?new_search=true'],
                ['ليست شعب بانک ها', 'bankBranch.do?new_search=true'],
                ['حساب های بانکی شعب','bankAccount.do?new_search=true'],
                ['ليست فرمت های چک', 'checkFormat.do?new_search=true'],
                ['صندوق‌ها', 'cash.do?new_search=true']
        ]
];
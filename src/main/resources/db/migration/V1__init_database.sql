CREATE TABLE public.users
(
    id         integer NOT NULL,
    email      character varying(255),
    first_name character varying(255),
    password   character varying(255),
    role_id    integer
);


ALTER TABLE public.users
    OWNER TO postgres;


CREATE SEQUENCE public.customer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.customer_id_seq
    OWNER TO postgres;

--
-- Name: customer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.customer_id_seq OWNED BY public.users.id;


--
-- Name: menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.menu
(
    id          integer NOT NULL,
    category    integer,
    description character varying(255),
    name        character varying(255),
    price       double precision
);


ALTER TABLE public.menu
    OWNER TO postgres;

--
-- Name: menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.menu_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.menu_id_seq
    OWNER TO postgres;

--
-- Name: menu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.menu_id_seq OWNED BY public.menu.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles
(
    role_id integer NOT NULL,
    name    character varying
);


ALTER TABLE public.roles
    OWNER TO postgres;

--
-- Name: menu id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.menu
    ALTER COLUMN id SET DEFAULT nextval('public.menu_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ALTER COLUMN id SET DEFAULT nextval('public.customer_id_seq'::regclass);


--
-- Data for Name: menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.menu (id, category, description, name, price) FROM stdin;
1	2	Помідори, сир, томатна паста	Margarita	180
2	6	Яйця скрембл або яєшня, Смажений бекон , Нут відварний, Кіноа, Чері Листя салату, Пармезан, Грінки з бріошного хліба	Скрембл	175
3	6	Помідори свіжі, Помідори пелаті , Цибуля фіолетова, Перець болгарський, Часник, Яйця, Сир фета, Зелень, Піта	Шакшука з фетою	175
4	6	Панкейки, шоколад, банан, рисові кульки	Панкейки з шоколадом та бананом	95
5	4	Рис басматі, яйця, броколі, соєвий соус, морква, імбир, часник, кунжут	Смажений рис з тофу	110
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (role_id, name) FROM stdin;
1	ADMIN
2	USER
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, first_name, password, role_id) FROM stdin;
1	user@gmail.com	john	$2a$10$2ClKI0sxQPwhpAULtQlsGenxaIqIBjg3GCw.hWVkFsOEZm.xEPkau	2
2	maryses811@gmail.com	maryna	$2a$10$61sBFwMXR8V13gzdQOeOHO7Z6l0WZELJCbeVppBzUWciQrcjzqEVm	2
5	admin@gmail.com	Admin	$2a$10$qnFrcdflnPr/74eBNlvKruMlhK152V4c2tNVYI/vlLgRn9nspHpe2	1
8	anna@gmail.com	Anna	$2a$10$30IxA0uU.s9k7E3qOqPiPu3q/rzflnltjJbzZOP0ZT2Hr.9H7K5km	2
\.


--
-- Name: customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customer_id_seq', 13, true);


--
-- Name: menu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.menu_id_seq', 5, true);


--
-- Name: users customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- Name: menu menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.menu
    ADD CONSTRAINT menu_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);


--
-- Name: users fkp56c1712k691lhsyewcssf40f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES public.roles (role_id);


--
-- PostgreSQL database dump complete
--


--
-- PostgreSQL database dump
--

\restrict s6OxdwrIA4oBWRHD8yhJTmKBncsGYvMduCyJhhdKRnLjkEAcQdySKe7Pr2he6Gu

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: get_dashboard_stats(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_dashboard_stats() RETURNS TABLE(total_jenis_obat bigint, total_stok integer, total_nilai_stok numeric, obat_menipis bigint, obat_kadaluarsa bigint)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT 
        COUNT(*)::BIGINT,
        COALESCE(SUM(o.stok_saat_ini), 0)::INTEGER,
        COALESCE(SUM(o.stok_saat_ini * o.harga_beli), 0),
        (SELECT COUNT(*) FROM view_stok_menipis)::BIGINT,
        (SELECT COUNT(*) FROM view_obat_kadaluarsa)::BIGINT
    FROM obat o;
END;
$$;


ALTER FUNCTION public.get_dashboard_stats() OWNER TO postgres;

--
-- Name: update_stok_keluar(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_stok_keluar() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE obat 
    SET stok_saat_ini = stok_saat_ini - NEW.jumlah,
        updated_at = CURRENT_TIMESTAMP
    WHERE id_obat = NEW.id_obat;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_stok_keluar() OWNER TO postgres;

--
-- Name: update_stok_masuk(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_stok_masuk() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE obat 
    SET stok_saat_ini = stok_saat_ini + NEW.jumlah,
        updated_at = CURRENT_TIMESTAMP
    WHERE id_obat = NEW.id_obat;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_stok_masuk() OWNER TO postgres;

--
-- Name: validate_stok(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.validate_stok() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.stok_saat_ini < 0 THEN
        RAISE EXCEPTION 'Stok tidak boleh negatif!';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.validate_stok() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: obat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.obat (
    id_obat integer NOT NULL,
    kode_obat character varying(20) NOT NULL,
    nama_obat character varying(200) NOT NULL,
    kategori character varying(50),
    satuan character varying(20) NOT NULL,
    deskripsi text,
    harga_beli numeric(12,2) NOT NULL,
    stok_saat_ini integer DEFAULT 0,
    stok_minimum integer DEFAULT 10,
    tanggal_kadaluarsa date,
    lokasi_penyimpanan character varying(100),
    produsen character varying(100),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by integer
);


ALTER TABLE public.obat OWNER TO postgres;

--
-- Name: obat_id_obat_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.obat_id_obat_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.obat_id_obat_seq OWNER TO postgres;

--
-- Name: obat_id_obat_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.obat_id_obat_seq OWNED BY public.obat.id_obat;


--
-- Name: obat_keluar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.obat_keluar (
    id_obat_keluar integer NOT NULL,
    id_obat integer NOT NULL,
    tanggal_keluar date NOT NULL,
    jumlah integer NOT NULL,
    tujuan character varying(100),
    keterangan text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by integer
);


ALTER TABLE public.obat_keluar OWNER TO postgres;

--
-- Name: obat_keluar_id_obat_keluar_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.obat_keluar_id_obat_keluar_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.obat_keluar_id_obat_keluar_seq OWNER TO postgres;

--
-- Name: obat_keluar_id_obat_keluar_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.obat_keluar_id_obat_keluar_seq OWNED BY public.obat_keluar.id_obat_keluar;


--
-- Name: obat_masuk; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.obat_masuk (
    id_obat_masuk integer NOT NULL,
    id_obat integer NOT NULL,
    tanggal_masuk date NOT NULL,
    jumlah integer NOT NULL,
    nomor_batch character varying(50),
    supplier character varying(100),
    keterangan text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_by integer
);


ALTER TABLE public.obat_masuk OWNER TO postgres;

--
-- Name: obat_masuk_id_obat_masuk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.obat_masuk_id_obat_masuk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.obat_masuk_id_obat_masuk_seq OWNER TO postgres;

--
-- Name: obat_masuk_id_obat_masuk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.obat_masuk_id_obat_masuk_seq OWNED BY public.obat_masuk.id_obat_masuk;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id_user integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    nama_lengkap character varying(100) NOT NULL,
    role character varying(20) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_user_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_user_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_user_seq OWNER TO postgres;

--
-- Name: users_id_user_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_user_seq OWNED BY public.users.id_user;


--
-- Name: view_laporan_stok; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_laporan_stok AS
 SELECT id_obat,
    kode_obat,
    nama_obat,
    kategori,
    satuan,
    stok_saat_ini,
    stok_minimum,
    harga_beli,
    ((stok_saat_ini)::numeric * harga_beli) AS nilai_stok,
        CASE
            WHEN (stok_saat_ini <= stok_minimum) THEN 'Menipis'::text
            WHEN ((stok_saat_ini)::numeric <= ((stok_minimum)::numeric * 1.5)) THEN 'Perhatian'::text
            ELSE 'Aman'::text
        END AS status_stok
   FROM public.obat o
  ORDER BY
        CASE
            WHEN (stok_saat_ini <= stok_minimum) THEN 'Menipis'::text
            WHEN ((stok_saat_ini)::numeric <= ((stok_minimum)::numeric * 1.5)) THEN 'Perhatian'::text
            ELSE 'Aman'::text
        END DESC, nama_obat;


ALTER VIEW public.view_laporan_stok OWNER TO postgres;

--
-- Name: view_obat_kadaluarsa; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_obat_kadaluarsa AS
 SELECT id_obat,
    kode_obat,
    nama_obat,
    tanggal_kadaluarsa,
    (tanggal_kadaluarsa - CURRENT_DATE) AS hari_tersisa
   FROM public.obat o
  WHERE ((tanggal_kadaluarsa IS NOT NULL) AND ((tanggal_kadaluarsa >= CURRENT_DATE) AND (tanggal_kadaluarsa <= (CURRENT_DATE + '90 days'::interval))))
  ORDER BY tanggal_kadaluarsa;


ALTER VIEW public.view_obat_kadaluarsa OWNER TO postgres;

--
-- Name: view_rekap_bulanan; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_rekap_bulanan AS
 SELECT to_char((om.tanggal_masuk)::timestamp with time zone, 'YYYY-MM'::text) AS bulan,
    count(DISTINCT om.id_obat) AS jenis_obat_masuk,
    sum(om.jumlah) AS total_obat_masuk,
    count(ok.id_obat_keluar) AS jumlah_transaksi_keluar,
    COALESCE(sum(ok.jumlah), (0)::bigint) AS total_obat_keluar
   FROM (public.obat_masuk om
     LEFT JOIN public.obat_keluar ok ON ((to_char((om.tanggal_masuk)::timestamp with time zone, 'YYYY-MM'::text) = to_char((ok.tanggal_keluar)::timestamp with time zone, 'YYYY-MM'::text))))
  GROUP BY (to_char((om.tanggal_masuk)::timestamp with time zone, 'YYYY-MM'::text))
  ORDER BY (to_char((om.tanggal_masuk)::timestamp with time zone, 'YYYY-MM'::text)) DESC;


ALTER VIEW public.view_rekap_bulanan OWNER TO postgres;

--
-- Name: view_stok_menipis; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_stok_menipis AS
 SELECT id_obat,
    kode_obat,
    nama_obat,
    kategori,
    satuan,
    stok_saat_ini,
    stok_minimum,
    (stok_minimum - stok_saat_ini) AS selisih
   FROM public.obat o
  WHERE (stok_saat_ini <= stok_minimum)
  ORDER BY (stok_minimum - stok_saat_ini) DESC;


ALTER VIEW public.view_stok_menipis OWNER TO postgres;

--
-- Name: view_top_obat_laku; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_top_obat_laku AS
 SELECT o.kode_obat,
    o.nama_obat,
    o.kategori,
    sum(ok.jumlah) AS total_keluar
   FROM (public.obat o
     JOIN public.obat_keluar ok ON ((o.id_obat = ok.id_obat)))
  GROUP BY o.id_obat, o.kode_obat, o.nama_obat, o.kategori
  ORDER BY (sum(ok.jumlah)) DESC
 LIMIT 10;


ALTER VIEW public.view_top_obat_laku OWNER TO postgres;

--
-- Name: obat id_obat; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat ALTER COLUMN id_obat SET DEFAULT nextval('public.obat_id_obat_seq'::regclass);


--
-- Name: obat_keluar id_obat_keluar; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_keluar ALTER COLUMN id_obat_keluar SET DEFAULT nextval('public.obat_keluar_id_obat_keluar_seq'::regclass);


--
-- Name: obat_masuk id_obat_masuk; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_masuk ALTER COLUMN id_obat_masuk SET DEFAULT nextval('public.obat_masuk_id_obat_masuk_seq'::regclass);


--
-- Name: users id_user; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id_user SET DEFAULT nextval('public.users_id_user_seq'::regclass);


--
-- Data for Name: obat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.obat (id_obat, kode_obat, nama_obat, kategori, satuan, deskripsi, harga_beli, stok_saat_ini, stok_minimum, tanggal_kadaluarsa, lokasi_penyimpanan, produsen, created_at, updated_at, created_by) FROM stdin;
6	OBT006	Betadine Solution	Antiseptik	Botol	Antiseptik luka	25000.00	60	30	2026-05-15	Rak E1	Mundipharma	2025-12-05 16:57:19.072905	2025-12-05 16:57:19.072905	1
1	OBT001	Paracetamol 500mg	Analgesik	Strip	Obat penurun panas dan pereda nyeri	5000.00	140	50	2025-12-31	Rak A1	Kimia Farma	2025-12-05 16:57:19.072905	2025-12-05 16:57:19.072905	1
2	OBT002	Amoxicillin 500mg	Antibiotik	Strip	Antibiotik untuk infeksi bakteri	12000.00	60	30	2025-10-15	Rak A2	Sanbe	2025-12-05 16:57:19.072905	2025-12-05 16:57:19.072905	1
3	OBT003	Antasida Tablet	Antasida	Strip	Obat maag dan asam lambung	3000.00	55	40	2026-03-20	Rak B1	Dexa Medica	2025-12-05 16:57:19.072905	2025-12-05 16:57:19.072905	1
5	OBT005	OBH Combi Sirup	Obat Batuk	Botol	Sirup obat batuk berdahak	18000.00	30	25	2025-11-10	Rak D1	OBH	2025-12-05 16:57:19.072905	2025-12-11 15:00:31.561854	1
4	OBT004	Vitamin C 100mg	Vitamin	Botol	Suplemen vitamin C	15000.00	18	20	2025-08-30	Rak C1	Kalbe	2025-12-05 16:57:19.072905	2025-12-11 15:00:50.524682	1
\.


--
-- Data for Name: obat_keluar; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.obat_keluar (id_obat_keluar, id_obat, tanggal_keluar, jumlah, tujuan, keterangan, created_at, created_by) FROM stdin;
1	1	2024-02-01	10	Poliklinik Umum	Pasien rawat jalan	2025-12-05 16:57:19.072905	2
2	2	2024-02-05	15	UGD	Stok darurat UGD	2025-12-05 16:57:19.072905	2
3	3	2024-02-15	5	Poliklinik Gigi	Pasien gigi	2025-12-05 16:57:19.072905	2
\.


--
-- Data for Name: obat_masuk; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.obat_masuk (id_obat_masuk, id_obat, tanggal_masuk, jumlah, nomor_batch, supplier, keterangan, created_at, created_by) FROM stdin;
1	1	2024-01-15	50	BATCH-2024-001	PT Distributor Farma	Pengadaan rutin Januari	2025-12-05 16:57:19.072905	1
2	2	2024-01-20	30	BATCH-2024-002	PT Distributor Farma	Pengadaan rutin Januari	2025-12-05 16:57:19.072905	1
3	3	2024-02-10	40	BATCH-2024-003	CV Medika Jaya	Restock obat maag	2025-12-05 16:57:19.072905	1
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id_user, username, password, nama_lengkap, role, created_at) FROM stdin;
2	apoteker1	$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy	Apoteker Rina	Apoteker	2025-12-05 16:57:19.072905
3	staff1	$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy	Staff Budi	Staff	2025-12-05 16:57:19.072905
1	admin	admin123	Administrator	Admin	2025-12-05 16:57:19.072905
\.


--
-- Name: obat_id_obat_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.obat_id_obat_seq', 7, true);


--
-- Name: obat_keluar_id_obat_keluar_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.obat_keluar_id_obat_keluar_seq', 3, true);


--
-- Name: obat_masuk_id_obat_masuk_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.obat_masuk_id_obat_masuk_seq', 3, true);


--
-- Name: users_id_user_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_user_seq', 3, true);


--
-- Name: obat_keluar obat_keluar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_keluar
    ADD CONSTRAINT obat_keluar_pkey PRIMARY KEY (id_obat_keluar);


--
-- Name: obat obat_kode_obat_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat
    ADD CONSTRAINT obat_kode_obat_key UNIQUE (kode_obat);


--
-- Name: obat_masuk obat_masuk_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_masuk
    ADD CONSTRAINT obat_masuk_pkey PRIMARY KEY (id_obat_masuk);


--
-- Name: obat obat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat
    ADD CONSTRAINT obat_pkey PRIMARY KEY (id_obat);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: idx_obat_kategori; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_obat_kategori ON public.obat USING btree (kategori);


--
-- Name: idx_obat_keluar_tanggal; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_obat_keluar_tanggal ON public.obat_keluar USING btree (tanggal_keluar);


--
-- Name: idx_obat_kode; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_obat_kode ON public.obat USING btree (kode_obat);


--
-- Name: idx_obat_masuk_tanggal; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_obat_masuk_tanggal ON public.obat_masuk USING btree (tanggal_masuk);


--
-- Name: idx_obat_nama; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_obat_nama ON public.obat USING btree (nama_obat);


--
-- Name: obat_keluar trg_obat_keluar; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_obat_keluar AFTER INSERT ON public.obat_keluar FOR EACH ROW EXECUTE FUNCTION public.update_stok_keluar();


--
-- Name: obat_masuk trg_obat_masuk; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_obat_masuk AFTER INSERT ON public.obat_masuk FOR EACH ROW EXECUTE FUNCTION public.update_stok_masuk();


--
-- Name: obat trg_validate_stok; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_validate_stok BEFORE UPDATE ON public.obat FOR EACH ROW EXECUTE FUNCTION public.validate_stok();


--
-- Name: obat obat_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat
    ADD CONSTRAINT obat_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.users(id_user);


--
-- Name: obat_keluar obat_keluar_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_keluar
    ADD CONSTRAINT obat_keluar_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.users(id_user);


--
-- Name: obat_keluar obat_keluar_id_obat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_keluar
    ADD CONSTRAINT obat_keluar_id_obat_fkey FOREIGN KEY (id_obat) REFERENCES public.obat(id_obat) ON DELETE CASCADE;


--
-- Name: obat_masuk obat_masuk_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_masuk
    ADD CONSTRAINT obat_masuk_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.users(id_user);


--
-- Name: obat_masuk obat_masuk_id_obat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obat_masuk
    ADD CONSTRAINT obat_masuk_id_obat_fkey FOREIGN KEY (id_obat) REFERENCES public.obat(id_obat) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict s6OxdwrIA4oBWRHD8yhJTmKBncsGYvMduCyJhhdKRnLjkEAcQdySKe7Pr2he6Gu


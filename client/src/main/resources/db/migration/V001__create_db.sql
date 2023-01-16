CREATE TABLE client (
   id uuid NOT NULL,
   name character varying(255),
   email character varying(255),
   cpf character varying(255),
   CONSTRAINT client_pkey PRIMARY KEY (id)
);
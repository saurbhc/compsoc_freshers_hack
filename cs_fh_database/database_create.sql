-- Table: public.video

-- DROP TABLE public.video;

CREATE TABLE IF NOT EXISTS public.video
(
    video_id uuid NOT NULL,
    link text COLLATE pg_catalog."default" NOT NULL,
    created_at text COLLATE pg_catalog."default",
    audio_link text COLLATE pg_catalog."default",
    CONSTRAINT video_pkey PRIMARY KEY (video_id),
    CONSTRAINT unique_video_link UNIQUE (link)
)

TABLESPACE pg_default;

ALTER TABLE public.video
    OWNER to postgres;


-- Table: public.video_word

-- DROP TABLE public.video_word;

CREATE TABLE IF NOT EXISTS public.video_word
(
    video_word_id uuid NOT NULL,
    video_id uuid NOT NULL,
    word text COLLATE pg_catalog."default" NOT NULL,
    seconds bigint NOT NULL,
    transcription_id uuid,
    CONSTRAINT video_word_pkey PRIMARY KEY (video_word_id),
    CONSTRAINT video_id_constraint FOREIGN KEY (video_id)
        REFERENCES public.video (video_id) MATCH SIMPLE
        ON UPDATE SET DEFAULT
        ON DELETE SET DEFAULT
)

TABLESPACE pg_default;

ALTER TABLE public.video_word
    OWNER to postgres;
-- Index: search_word

-- DROP INDEX public.search_word;

CREATE INDEX search_word
    ON public.video_word USING btree
    (word COLLATE pg_catalog."aa_DJ" varchar_ops ASC NULLS LAST)
    TABLESPACE pg_default;


-- Table: public.transcription

-- DROP TABLE public.transcription;

CREATE TABLE IF NOT EXISTS public.transcription
(
    transcription_id uuid NOT NULL,
    transcription_text text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT transcription_pkey PRIMARY KEY (transcription_id)
)

TABLESPACE pg_default;

ALTER TABLE public.transcription
    OWNER to postgres;
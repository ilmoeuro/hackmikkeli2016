CREATE TABLE "PUBLIC"."PLAN" (
    "pk"                INTEGER             IDENTITY(1,1),
    "id"                UUID                NOT NULL,
    "deleted"           BIT(1)              NOT NULL,
    "title"             VARCHAR(255)        NOT NULL,
    "description"       VARCHAR(4096)       NOT NULL,
    "image_width"       INTEGER             NOT NULL,
    "image_height"      INTEGER             NOT NULL,
    "tile_size"         INTEGER             NOT NULL,
    CONSTRAINT "$plan_pk"
        PRIMARY KEY ("pk"),
    CONSTRAINT "$plan_u_id"
        UNIQUE ("id"),
    CONSTRAINT "$plan_c_image_width_gt0"
        CHECK ("image_width" > 0),
    CONSTRAINT "$plan_c_image_height_gt0"
        CHECK ("image_height" > 0),
    CONSTRAINT "$plan_c_tile_size_gt0"
        CHECK ("tile_size" > 0)
);

CREATE TABLE "PUBLIC"."PLAN_PROPOSAL" (
    "pk"                INTEGER             IDENTITY(1,1),
    "id"                UUID                NOT NULL,
    "deleted"           BIT(1)              NOT NULL,
    "title"             VARCHAR(255)        NOT NULL,
    "description"       VARCHAR(4096)       NOT NULL,
    "filename"          VARCHAR(4096)       NOT NULL,
    "plan_id"           UUID                NOT NULL,
    CONSTRAINT "$plan_proposal_pk"
        PRIMARY KEY ("pk"),
    CONSTRAINT "$plan_proposal_u_id"
        UNIQUE ("id"),
    CONSTRAINT "$plan_proposal_fk_plan_id"
        FOREIGN KEY ("plan_id") REFERENCES "PLAN" ("id")
);

CREATE TABLE "PUBLIC"."PLAN_VOTE" (
    "pk"                INTEGER             IDENTITY(1,1),
    "id"                UUID                NOT NULL,
    "deleted"           BIT(1)              NOT NULL,
    "proposal_id"       UUID                NOT NULL,
    "tile_number"       INTEGER             NOT NULL,
    CONSTRAINT "$plan_vote_pk"
        PRIMARY KEY ("pk"),
    CONSTRAINT "$plan_vote_u_id"
        UNIQUE ("id"),
    CONSTRAINT "$plan_vote_fk_proposal_id"
        FOREIGN KEY ("proposal_id") REFERENCES "PLAN_PROPOSAL" ("id"),
    CONSTRAINT "$plan_vote_c_tile_number_gt0"
        CHECK ("tile_number" > 0)
);
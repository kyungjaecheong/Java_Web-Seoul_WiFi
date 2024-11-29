create table bookmark_group
(
    id            INTEGER,
    name          TEXT,
    order_no      INTEGER,
    register_dttm TEXT,
    update_dttm   TEXT,
    primary key (id)
);

create table public_wifi
(
    id                  INTEGER,
    x_swifi_mgr_no      TEXT,
    x_swifi_wrdofc      TEXT,
    x_swifi_main_nm     TEXT,
    x_swifi_adres1      TEXT,
    x_swifi_adres2      TEXT,
    x_swifi_instl_floor TEXT,
    x_swifi_instl_ty    TEXT,
    x_swifi_instl_mby   TEXT,
    x_swifi_svc_se      TEXT,
    x_swifi_cmcwr       TEXT,
    x_swifi_cnstc_year  TEXT,
    x_swifi_inout_door  TEXT,
    x_swifi_remars3     TEXT,
    lat                 TEXT,
    lnt                 TEXT,
    work_dttm           TIMESTAMP,
    primary key (id autoincrement)
);

create table bookmark_list
(
    id            INTEGER,
    group_no      INTEGER,
    mgr_no        TEXT,
    register_dttm TEXT,
    primary key (id),
    constraint group_fk
        foreign key (group_no) references bookmark_group (id),
    constraint mgr_fk
        foreign key (mgr_no) references public_wifi (x_swifi_mgr_no)
);

create table search_wifi
(
    id          INTEGER,
    lat         TEXT,
    lnt         TEXT,
    search_dttm TEXT,
    primary key (id)
);

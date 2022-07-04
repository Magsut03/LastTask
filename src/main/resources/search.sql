-- for collection --
alter table collection add column doc tsvector;

create index doc_coll_index on collection using gin (doc);

update collection set doc = to_tsvector(name || ' ' || description);

create function collection_tsvektor_trigger() returns trigger as
$$
BEGIN
    new.doc := setweight(to_tsvector(new.name), 'B') ||
               setweight(to_tsvector(new.description), 'A');
return new;
END
$$ language plpgsql;

create trigger coll_tsvektor_update
    before insert or update
                         on collection
                         for each row
                         execute procedure collection_tsvektor_trigger();





-- for item --
alter table items add column doc tsvector;

create index doc_item_index on items using gin (doc);

update items set doc = to_tsvector(name);

create function item_tsvektor_trigger() returns trigger as
$$
BEGIN
    new.doc := to_tsvector(new.name);
return new;
END
$$ language plpgsql;

create trigger item_tsvektor_update
    before insert or update
                         on items
                         for each row
                         execute procedure item_tsvektor_trigger();




-- for comment --
alter table comment add column doc tsvector;

create index doc_comment_index on comment using gin (doc);

update comment set doc = to_tsvector(comment.text);

create function comment_tsvektor_trigger() returns trigger as
$$
BEGIN
    new.doc := to_tsvector(new.text);
return new;
END
$$ language plpgsql;

create trigger comment_tsvektor_update
    before insert or update
                         on comment
                         for each row
                         execute procedure item_tsvektor_trigger();
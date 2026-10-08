-- DROP FUNCTION java.restart_seq();

CREATE OR REPLACE FUNCTION java.restart_seq()
 RETURNS integer
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $function$
	BEGIN
		execute 'ALTER SEQUENCE java.users_id_seq RESTART WITH 1';
		execute 'ALTER SEQUENCE java.products_id_seq RESTART WITH 1';
		return 0;
	END;
$function$
;

-- Permissions

ALTER FUNCTION java.restart_seq() OWNER TO dbman;
GRANT ALL ON FUNCTION java.restart_seq() TO dbman;
GRANT ALL ON FUNCTION java.restart_seq() TO java;

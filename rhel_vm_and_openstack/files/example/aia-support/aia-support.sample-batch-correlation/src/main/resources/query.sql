SELECT
fuzzy.Adjacency4GName AS ADJACENCY_4G_NAME,
split_part(ADJACENCY4GNAME, '#', 1) AS CELLA,
split_part(ADJACENCY4GNAME, '#', 2) AS CELLB,
cella_cm.CELLA_CM_ELEMENTNAME,
cellb_cm.CELLB_CM_ELEMENTNAME
FROM
(
 SELECT
 ELEMENTNAME AS CELLA_CM_ELEMENTNAME
 FROM cmecell,
 (
  SELECT DISTINCT
  split_part(ADJACENCY4GNAME, '#', 1) AS name
  FROM fuzzy
 )
 AS cella_list
 WHERE cella_list.name = cmecell.ELEMENTNAME
)
AS cella_cm,
(
 SELECT
 ELEMENTNAME AS CELLB_CM_ELEMENTNAME
 FROM cmecell,
 (
  SELECT DISTINCT
  split_part(ADJACENCY4GNAME, '#', 2) AS name
  FROM fuzzy
 )
 AS cellb_list
 WHERE cellb_list.name = cmecell.ELEMENTNAME
)
AS cellb_cm,
fuzzy
WHERE
split_part(ADJACENCY4GNAME, '#', 1) = cella_cm.CELLA_CM_ELEMENTNAME
AND
split_part(ADJACENCY4GNAME, '#', 2) = cellb_cm.CELLB_CM_ELEMENTNAME

/* split(fuzzy.ADJACENCY4GNAME , '#')[0] */

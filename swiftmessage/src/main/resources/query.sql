(Select m.country_of_origin,s.customer_type , SUM(s.birr_amount) as summ FROM
tbl_incoming_record m INNER JOIN swift_transaction s
ON s.message = m.id
WHERE
m.reg_date BETWEEN $P{startdate}  AND $P{enddate}
GROUP BY s.customer_type,m.country_of_origin);

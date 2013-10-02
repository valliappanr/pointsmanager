PointsManager - Cassandra / Astyanax
====================================

Overview:
=========
	This project provides a sample implementation of Points accruel of a member within loyalty management platform using Cassandra and Astyanax. Scenarios are,
		1. Member can be awarded points through the system. When a member does shopping at a shop, points get awarded to them based on the type of product they buy. In this case, need to
		   handle storing of points for a member on a particular time and also for which product category they have been awarded with points.
		2. Member can use the award points for buying things, in which case the points get deducted from member account. The assumption here is that frequency of
		   deducting points are much compared to adding points to the member.
		   

The cassandra Column family structures are below,

	KeySpace - pointsbank
	
	Column Family - transaction - dynamic column family

	|-------------------------------------------------
	| Row                   | column name: timestamp |
	| memberId:<date>       | value:  <points>       |
	|                       |                        |
	|------------------------------------------------


	Column Family - product_category - dynamic column family

	|----------------------------------------------------------------
	| Row                   | column name: Composite<memberId:date> |
	| categoryId:<date>     | value:  <points>                      |
	|                       |                                       |
	|----------------------------------------------------------------

	Column Family - monthly_txn - CounterType column

	|------------------------------------------------------------------|
	| Row                   | column name: sum                         |
	| memberId:<month-year> | value: <Sum accumulated for the current  |
	|                       | month.                                   |
	|------------------------------------------------------------------|


	1. transaction column family stores transaction made by each member on a particular day (this includes both accumulating points, and use the points).
	2. product_category column family stores for the category(food, clothes, electric items) in which member been awarded with points.
	3. monthly_txn column family stores the accumulated points of a member for specific month. This has to be calculated and updated overnight in a background process from the daily transactions of a member.


Sample Request / Response:
=========================

	1. Adding points:
	-----------------

	Request ("MEMBER-0001", 30, Electronics)
	
	Based on current time("2013-09-02 10:00:00") the transaction ColumnFamily will be added with new new column entry:


	Column Family - transaction

	|--------------------------------------------------------
	| Row				| 2013-09-02 10:00:00	|
	| MEMBER-0001:2013-09-02	| 30	 		|
	|				|			|
	|--------------------------------------------------------


	product_category  Column family gets updated in the same way for the awarded points,

	Column Family - product_category

	|------------------------------------------------------------------------
	| Row				| MEMBER-0001:2013-09-02 10:00:00	|
	| ELECTRONICS:2013-09-02	| 30	 				|
	|				|					|
	|------------------------------------------------------------------------


	Column Family - monthly_txn - CounterType column 

	|------------------------------------------------------------------|
	| Row			| sum 			   		   |
	| memberId:2013-09	| 50 					   |
	|			| 	  			           |
	|------------------------------------------------------------------|

	2. Deducting points:
	--------------------

	Request ("MEMBER-0001", 10) - request comes in at (2013-09-02 10:10:00)
	
	First aggregation of awarded points of a member on that day being calculated and added to the monthly_txn count (which is the aggregated sum of that month till previous day).
	Then compared the aggregated sum to the points to be deducted, if more points are there in the member account, points get deducted.

	
	Column Family - transaction

	|------------------------------------------------------------------------------	|
	| Row				| 2013-09-02 10:00:00	| 2013-09-02 10:10:00	|
	| MEMBER-0001:2013-09-02	| 30	 		| -10			|
	|				|			|			|
	|------------------------------------------------------------------------------	|


	3. Monthly Transaction Aggregation : (Not done yet)
	------------------------------------

	1. A nightly scheduled job runs to calculate the total points of a member until the previous day. Finds all the transactions of a member on that day and calculate the sum of the transaction and update the
	   monthly_txn with aggregated value on the previous day.


	Column Family - monthly_txn - CounterType column 

	|------------------------------------------------------------------|
	| Row			| sum 			   		   |
	| memberId:2013-09	| 70 (50 + 30 -10) 			   |
	|			| 	  			           |
	|------------------------------------------------------------------|

	


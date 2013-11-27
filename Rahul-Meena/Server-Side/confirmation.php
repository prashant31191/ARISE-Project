<?php  //confirmation.php

require_once 'userslogin.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());
mysql_select_db($db_database, $db_server)
or die("Unable to select database: " . mysql_error());

if(isset($_GET['passkey']){
// Passkey that got from link 
$passkey=$_GET['passkey'];
$tbl_name="user";

// Retrieve data from table where row that match this passkey 
$sql="SELECT * FROM $tbl_name WHERE confirm_code ='$passkey'";
$result=mysql_query($sql);

// Count how many row has this passkey
$count=mysql_num_rows($result);

// If successfully queried 
if($result && $count==1){

// if found this passkey in our database, retrieve data from table "temp_members_db"
$rows=mysql_fetch_array($result);
$email=$rows['email'];

//set Valid column to 1 and Code to 0
$valid= 1;
$code = 0;

// Insert data that retrieves from "temp_members_db" into table "registered_members" 
$sql2="INSERT INTO $tbl_name(valid,code)VALUES('$valid','$code') WHERE email='$email'";
$result2=mysql_query($sql2);
}

else{
	echo "No such account exist!";
}
}

// if not found passkey, display message "Wrong Confirmation code" 
else {
echo "Wrong Confirmation code";
}

// if successfully moved data from table"temp_members_db" to table "registered_members" displays message "Your account has been activated" and don't forget to delete confirmation code from table "temp_members_db"
if($result2){

echo "Your account has been activated";

}

?>
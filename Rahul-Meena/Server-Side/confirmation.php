<?php  //confirmation.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

if(isset($_GET['passkey'])){
	// Passkey that got from link 
	$passkey=$_GET['passkey'];

	// Retrieve data from table where row that match this passkey 
	$sql="SELECT * FROM $db->$tbl_temp_user WHERE code ='$passkey'";
	$result=mysql_query($sql);

	// Count how many row has this passkey
	$count=mysql_num_rows($result);

	// If successfully queried 
	if($result && $count==1){
		// if found this passkey in our database, retrieve data from table
		$rows=mysql_fetch_array($result);
		$email=$rows['email'];
		// set Valid column to 1 and Code to 0
		$valid= 1;
		// Update valid value in original table user" 
		$sql1="UPDATE $db->$tbl_user SET valid = 1 WHERE email = '$email'";
		$result1=mysql_query($sql1);
		// Delete the entry against this email in temp_user table" 
		$sql2="DELETE FROM $db->$tbl_temp_user WHERE code='$passkey'";
		$result2=mysql_query($sql2);
	} else{
		echo "No such account exist!";
 	}
}

// if not found passkey, display message "Wrong Confirmation code" 
else {
echo "Wrong Confirmation code";
}

// if successfully moved data from table"temp_members_db" to table "registered_members" displays message "Your account has been activated" and don't forget to delete confirmation code from table "temp_members_db"
if($result2 && $result1){

echo "Your account has been activated";

}

?>
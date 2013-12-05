<?php  //change_password.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

if(isset($_GET['passkey'])){
	// Passkey that got from link 
	$passkey=$_GET['passkey'];

	// Retrieve data from table where row that match this passkey 
	$sql="SELECT uid FROM $db->$tbl_change_password_user WHERE code ='$passkey'";
	$result=mysql_query($sql);

	// Count how many row has this passkey
	$count=mysql_num_rows($result);

	// If successfully queried means password change request exist
	if($result && $count==1){
		//imput for email
		echo "
    	<form method="post">
		<input type="hidden" name="passkey" value='$passkey' />
        <input type="text" name="password" placeholder="Password" autofocus>
        <button  type="submit" action="is_password_changed.php">Submit</button>
        </form>"
	} else{
		echo "No such account exist!";
 	}
}

// if not found passkey, display message "Wrong Confirmation code" 
else {
echo "Wrong Confirmation code";
}

?>
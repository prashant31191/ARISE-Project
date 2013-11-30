<?php  //is_password_changed.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

if(isset($_POST['passkey']) && isset($_POST['password'])){
	// Passkey that got from link 
	$passkey=$_GET['passkey'];
	// email that guser gave 
	$password=$_GET['password'];

	$isPasswordChanged = $db->setNewPassword($passkey,$password);
	// check if new password is set
	if($isPasswordChanged){
		echo "Your new password is successfully set."
	} else{
		echo "Can't set you new password.";
 	}
} else {
echo "Required field(s) is missing";
}

?>
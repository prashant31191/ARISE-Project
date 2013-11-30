<?php //change_password.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['email'])) {
    $email = $_POST['email'];
    //check if the email is registered or not
    $check = $db->isUserExisted($email);
    //is registered
    if($check){
    	$change_password_link = $db->sendChangePasswordLink($email);
    	// check if mail is sent
    	if($change_password_link){
            // change password link is sent
            $response["success"] = 1;
            $response["message"] = "A link to change password is sent to your email.";
    	} else {
            // failed to send row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
    	}
    } else {
        // account is not registered
        $response["success"] = 0;
        $response["message"] = "This account is not registered.";

    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>
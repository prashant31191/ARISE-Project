<?php // user_exist.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['email'])) {

    $email = $_POST['email'];

        // inserting a new user
        $check = $db->userExist($email);
 
        // check if user inserted or not
        if ($check) {
            // confirmation link is sent
            $response["success"] = 1;
            $response["message"] = "User Email Already Exist";
        } else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "New User Email.";
        }

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>




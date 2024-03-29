<?php //register.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['gcm_regid'])) {
 
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $gcm_regid = $_POST['gcm_regid'];

        // inserting a new user
        $result = $db->createUser($name, $email, $password, $gcm_regid);
 
        // check if user inserted or not
        if ($result) {
            // confirmation link is sent
            $response["success"] = 1;
            $response["message"] = "User successfully registered and a confirmation link is sent to your email.";
        } else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
        }

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>




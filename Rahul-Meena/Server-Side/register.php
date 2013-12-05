<?php //register.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password'])) {
 
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    // check if user already existed
    // false = not existed
    $check = $db->isUserExisted($email);

    // new user
    if(!$check){

        // inserting a new user
        $result = $db->createUser($name, $email, $password);
 
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
        // account already registered
        $response["success"] = 0;
        $response["message"] = "This account is already registered.";

    }

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>




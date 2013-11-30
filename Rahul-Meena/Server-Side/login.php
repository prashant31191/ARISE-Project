<?php //login.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['email']) && isset($_POST['password'])) {
 
    $email = $_POST['email'];
    $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            $response["success"] = 1;
            $response["id"] = $user["id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["pwcsml"] = $user["pwcsml"];
            $response["user"]["pwlics"] = $user["pwlics"];
            echo json_encode($response);
        } else {
            // user not found
            $response["success"] = 0;
            $response["message"] = "Incorrect email or password!";
            echo json_encode($response);
        }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
?>
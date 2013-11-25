<?php //register.php

require_once 'userslogin.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());
mysql_select_db($db_database, $db_server)
or die("Unable to select database: " . mysql_error());

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['email'])) {
 
    $name = $_POST['name'];
    $email = $_POST['email'];


    // mysql inserting a new row
    $result = mysql_query("INSERT INTO user(name, email) VALUES('$name', '$email')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "User successfully created.";
 
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




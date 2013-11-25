<?php //allusers.php
require_once 'userslogin.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());
mysql_select_db($db_database, $db_server)
or die("Unable to select database: " . mysql_error());

// array for JSON response
$response = array();
 
// get all users from users table
$result = mysql_query("SELECT *FROM user") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // users node
    $response["users"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $user = array();
        $user["uid"] = $row["id"];
        $user["name"] = $row["name"];
        $user["email"] = $row["email"];
        $user["location"] = $row["location"];
 
        // push single user into final response array
        array_push($response["users"], $user);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no users found
    $response["success"] = 0;
    $response["message"] = "No users found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>
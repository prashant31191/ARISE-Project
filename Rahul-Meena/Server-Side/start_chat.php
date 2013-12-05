<?php // start_chat.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['gcm_regid1'])&& isset($_POST['gcm_regid2']) && isset($_POST['cpid'])) {

    $gcm_regid1 = $_POST['gcm_regid1'];
    $gcm_regid2 = $_POST['gcm_regid2'];
    $cpid = $_POST['cpid'];

    // get chat log of that day
    // false = not existed
    $chat_log = $db->getChatLogOfThatDay($cpid);

    // chat log found
    if($chat_log){
        // chat log found success
        $response["success"] = 1;
        $response["chat_log"] = array();
        // push pwlucs into final response array
        array_push($response["chat_log"], $chat_log["chat_log"]);
    } else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "No Previos Conversations.";
    }

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
}
    // echoing JSON response
    echo json_encode($response);
?>




<?php // send_message.php

// include db handler
require_once 'db_functions.php';
$db = new DB_Functions();

// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['sender'])&& isset($_POST['receiver']) && isset($_POST['cpid']) && isset($_POST['message'])) {

    $chat = array();
    $chat['sender'] = $_POST['sender'];
    $chat['receiver'] = $_POST['receiver'];
    $chat['cpid'] = $_POST['cpid'];
    $chat['message'] = $_POST['message'];

    // send chat message 
    $chat_log = $db->sendChatMessage($chat);

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




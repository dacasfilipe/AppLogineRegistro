<?php

if ($_SERVER['REQUEST_METHOD']=='POST'){
    
    $id = $_POST['id'];
    $photo = $_POST['photo'];
  
    $path = "profile_image/$id.jpeg";
    $finalPath = "http://192.168.1.103/applogin/".$path;

    require_once 'connect.php'; //import do arquivo connect.php

    $sql = "UPDATE usuarios SET photo='$finalPath' WHERE id='$id' ";

    if (mysqli_query($conn, $sql)){

        if ( file_put_contents ($path, base64_decode($photo) ) ){

            $result['sucess'] = "1";
            $result['message']= "sucess";

            echo json_encode($result);
            mysqli_close($conn);
        }
    } //fim if

    
}
?>
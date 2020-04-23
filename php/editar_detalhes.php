<?php

if ($_SERVER['REQUEST_METHOD']=='POST'){
    
    $nome = $_POST['nome'];
    $email = $_POST['email'];
    $id = $_POST['id'];

    require_once 'connect.php'; //import do arquivo connect.php

    $sql = "UPDATE usuarios SET nome='$nome', email='$email' WHERE id='$id' ";

    if (mysqli_query($conn, $sql)){
        $result["sucess"] = "1";
        $result["message"]= "sucess";

        echo json_encode($result);
        mysqli_close($conn);
    }

    } else {
        $result["sucess"] = "0";
        $result["message"]= "error";

        echo json_encode($result);
        mysqli_close($conn);

    }
?>
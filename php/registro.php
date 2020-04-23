<?php

if ($_SERVER['REQUEST_METHOD']=='POST'){

    $nome = $_POST['nome'];
    $email = $_POST['email'];
    $senha = $_POST['senha'];

    $senha = password_hash($senha, PASSWORD_DEFAULT);

    require_once 'connect.php'

    $sql = "INSERT INTO usuarios (nome, email, senha) VALUES ('$nome','$email','$senha')";

    if (mysqli_query($conn, $sql)){
        $result['sucess'] = "1";
        $result['message']= "sucess";

        echo json_encode($result);
        mysqli_close($conn);

    } else {
        $result['sucess'] = "0";
        $result['message']= "error";

        echo json_encode($result);
        mysqli_close($conn);

    }

}
?>
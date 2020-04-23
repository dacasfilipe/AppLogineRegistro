<?php
    if($_SERVER['REQUEST_METHOD']=='POST'){
        $email = $_POST['email'];
        $senha = $_POST['senha'];

        require_once 'connect.php';

        $sql = "SELECT * FROM usuarios WHERE email='$email' ";

        $response = mysqli_query($conn,$sql);

        $result = array();
        $result['login'] = array();

        if (mysqli_num_rows($response)=== 1){
            $row = mysqli_fetch_assoc($response);

            if (password_verify($senha, $row['senha'])){

                $index['nome'] = $row['nome'];
                $index['email'] = $row['email'];
                $index['id'] = $row['id'];

                array_push($result['login'], $index);

                $result['sucess'] = "1";
                $result['message'] = "sucess";
                echo json_encode($result);
                
                mysqli_close($conn);

            } else {
                $result['sucess'] = "0";
                $result['message'] = "erro";
                echo json_encode($result);
                
                mysqli_close($conn);
            }
        }
    }
?>
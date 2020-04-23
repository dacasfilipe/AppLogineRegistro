<?php

if ($_SERVER['REQUEST_METHOD']=='POST'){
    $id = $_POST['id'];

    require_once 'connect.php'; //import do arquivo connect.php

    $sql = "SELECT * FROM usuarios WHERE id='$id' ";

    $response = mysqli_query($conn,$sql);

    $result = array(); //cria uma lista de objetos
    $result['read'] = array(); // o objeto read também é transformado em uma lista de objetos

    if (mysqli_num_rows($response)=== 1){
       
       if ($row = mysqli_fetch_assoc($response)){

        $h['nome'] = $row['nome'];
        $h['email'] = $row['email'];

        array_push($result["read"], $h);

        $result["sucess"] = "1";
        echo json_encode($result);

        mysqli_close($conn);
        }
    }

}else {
    $result["sucess"] = "0";
    $result["message"] = "erro";
    echo json_encode($result);
                
    mysqli_close($conn);

}



?>
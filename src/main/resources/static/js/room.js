var stompClient = null
let lives = 0;
let guessWord = "";

function connect(){
    let room = document.getElementById('room').innerHTML;
    if(room){
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        alert("Connected");

        stompClient.connect({}, onConnected, onError);
    }
}

function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);
}

function onError() {
    alert("Error occurred");
}

function exit() {
    let username = document.getElementById('username').innerHTML;
    let room = document.getElementById('room').innerHTML;
    let defaultRoom = "default";
    let xhr = new XMLHttpRequest();
    xhr.responseType = 'text';

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            guessWord = 0;
            lives = 0;
            if(stompClient) {
                let chatMessage = {
                    room: room,
                    sender: username,
                    word: "",
                    type: 'SEND'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                stompClient.unsubscribe('/topic/public');
                stompClient = null;
            }
            window.location.href = "http://localhost:80/main/" + username;
        }
    }
    xhr.open("POST", "http://localhost:80/updateAttachment", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        username: username,
        room: defaultRoom
    }));
}

function send() {
    if(stompClient === null){
        alert("You are not connected");
        return;
    }
    let username = document.getElementById('username').innerHTML;
    if(guessWord !== ""){
        alert("The word is already guessed");
        return;
    }

    let room = document.getElementById('room').innerHTML;
    let word = document.getElementById("word").value;
    let space = ' ';

    if (word === "") {
        alert("Type the word");
        return;
    }

    if(word.includes(space)){
        alert("Guess the word without the spaces");
        return;
    }

    if(word && stompClient) {
        let chatMessage = {
            room: room,
            sender: username,
            word: word,
            type: 'SEND'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
    }
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let username = document.getElementById('username').innerHTML;
    let room = document.getElementById('room').innerHTML;

    if(message.room !== room || message.sender === username) {
        return;
    }

    guessWord = message.word;

    if(guessWord === "")
        return;

    document.getElementById("place").innerText = '_'.repeat(guessWord.length);
    alert("The secret word is received, now try to guess it, good luck");
    lives = 5;
    document.getElementById("lives").innerText = lives;
}

String.prototype.replaceAt = function(index, replacement) {
    return this.substring(0, index) + replacement + this.substring(index + 1);
}

function guess(){
    let room = document.getElementById('room').innerHTML;
    let letter = document.getElementById("letter").value;
    let username = document.getElementById('username').innerHTML;
    if(stompClient === null){
        alert("You are not connected");
        return;
    }
    if(guessWord === ""){
        alert("The word is not guessed");
        return;
    }
    if(letter === ""){
        alert("Choose the letter");
        return;
    }
    if(letter.length !== 1 || letter[0] < 'a' || letter[0] > 'z'){
        alert("You wrongly wrote the letter");
        return;
    }

    let text = document.getElementById("place").innerText;
    let flag = 0;

    for(let i = 0; i < guessWord.length; i++){
        if(text[i] === '_' && guessWord[i] === letter){
            text = text.replaceAt(i, letter);
            flag = 1;
        }
    }

    if(!flag){
        if(lives === 1){
            alert("You lost all your lives. You lost");
            lives = 0;
            document.getElementById("lives").innerText = lives;
            guessWord = "";
            document.getElementById('place').innerText = "";
            if(stompClient) {
                let chatMessage = {
                    room: room,
                    sender: username,
                    word: "",
                    type: 'SEND'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            }
        } else {
            alert("You guessed wrong, you lose one lifeline");
            lives--;
            document.getElementById("lives").innerText = lives;
        }
    } else {
        if(!text.includes('_')){
            alert("Congratulations, you guessed the word -" + text);
            lives = 0;
            guessWord = "";
            document.getElementById('place').innerText = "";
            document.getElementById("lives").innerText = lives;
            if(stompClient) {
                let chatMessage = {
                    room: room,
                    sender: username,
                    word: "",
                    type: 'SEND'
                };
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            }
        } else {
            document.getElementById("place").innerText = text;
        }
    }
}
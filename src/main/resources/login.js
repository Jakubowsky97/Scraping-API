async function login() {
    const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: 'Dawid000',
            password: 'test224'
        }),
        credentials: 'include' // pozwala na przesyłanie ciasteczek między domenami
    });
if (response.ok) {
        console.log('Zalogowano pomyślnie!');
    } else {
        console.error('Błąd logowania');
    }

    const data = await response.json();
    console.log(data);

    const test = getCookie("jwt");
    console.log(test);
}
login();


function getCookie(name) {
    const cookies = document.cookie.split('; ');
    for (let cookie of cookies) {
        const [key, value] = cookie.split('=');
        if (key === name) return value;
    }
    return null;
}
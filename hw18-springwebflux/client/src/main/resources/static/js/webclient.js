async function createClient() {
    const responseContainer = document.getElementById('createClientResponseContainer');
    responseContainer.innerHTML = "Waiting for request to be completed..."
    const nameContainer = document.getElementById('clientName');
    const addressContainer = document.getElementById('clientAddress');
    const phoneContainer = document.getElementById('clientPhone');
    const response = await fetch('/client/save', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: nameContainer.value,
            address: addressContainer.value,
            phone: phoneContainer.value
        })
    });
    const dataStream = await can.ndjsonStream(response.body);
    const reader = dataStream.getReader();
    const result = await reader.read();
    responseContainer.innerHTML = JSON.stringify(result.value);
}

const streamErr = e => {
    console.warn("error");
    console.warn(e);
}

fetch("/getClients").then((response) => {
    return can.ndjsonStream(response.body);
}).then(dataStream => {
    const reader = dataStream.getReader();
    const read = result => {
        if (result.done) {
            return;
        }
        render(result.value);
        reader.read().then(read, streamErr);
    }
    reader.read().then(read, streamErr);
});

const render = client => {
    const tbody = document.getElementById('tbody')
    const newRow = tbody.insertRow();
    newRow.insertCell().append(client.id);
    newRow.insertCell().append(client.name);
    newRow.insertCell().append(client.address);
    newRow.insertCell().append(client.phones);
};

// if research notes were taken previously, then get that notes from
// chrome local storage and display.
document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get(['researchNotes'], function(result) {
        if(result.researchNotes){
            document.getElementById('notes').value = result.researchNotes;
        }
    });

    document.getElementById('results').style.display = 'none';

    // document.getElementById('summarizeBtn').addEventListener('click', summarizeText);
    // document.getElementById('similarContentBtn').addEventListener("click", similarContent);
    document.getElementById('summarizeBtn').addEventListener("click", () => handleRequest("summarize"));
    document.getElementById('similarContentBtn').addEventListener("click", () => handleRequest("suggest"));
    document.getElementById('summarizePdfBtn').addEventListener("click", handlePDFRequest);
    document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);
});

/*
async function summarizeText() {
    // alert("summazie clicked")
    // 1. get selected text.
    // 2. call backend api.
    // 3. display result

    try {
        const [tab] = await chrome.tabs.query({active: true, currentWindow: true});
        const [{result}] = await chrome.scripting.executeScript({
            target: {tabId: tab.id},
            function: () => window.getSelection().toString()
        });

        if(!result) {
            showResult("Please select some text first!");
            return;
        }

        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({content: result, operation: 'summarize'})
        });
        
        if(!response.ok){
            throw new Error(`API Error: ${response.status}`);
        }

        const text = await response.text();
        // showResult(text.replace(/\n/g, '<br>')); // replace new line char with br tag. since we are working with html.
        showResult(text);

    } catch (error) {
        showResult(`Error: ${error.message}`);
    }
}


async function similarContent() {

    try{
        const [tab] = await chrome.tabs.query({active: true, currentWindow: true});
        const [{result}] = await chrome.scripting.executeScript({
            target: {tabId: tab.id},
            function: () => window.getSelection().toString()
        });

        if(!result){
            showResult("Please select some text first");
            return;
        }

        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({content: result, operation: 'suggest'})
        });

        if(!response.ok){
            throw new Error(`API Error: ${response.status}`);
        }

        const text = await response.text();
        showResult(text);
    }
    catch (error){
        showResult(`ErrorL ${error.message}`);
    }
}
*/

async function handleRequest(operation){
    try{
        const [tab] = await chrome.tabs.query({active: true, currentWindow: true});
        const [{result}] = await chrome.scripting.executeScript({
            target: {tabId: tab.id},
            function: () => window.getSelection().toString()
        });

        if(!result){
            showResult("Please select some text first");
            return;
        }

        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({content: result, operation: `${operation}`})
        });

        if(!response.ok){
            throw new Error(`API Error: ${response.status}`);
        }

        const text = await response.text();
        showResult(text);
    }
    catch (error){
        showResult(`Error: ${error.message}`);
    }
}

async function handlePDFRequest() {
    // 1. copy selected text to clipboard --emit
    // 2. pass this copied text to the API call
    // 3. serve response
    
    // let copiedText = window.navigator.clipboard.readText();
    
    try{
        let copiedText = await navigator.clipboard.readText();
        console.log("Clipboard text:", copiedText);
        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({content: copiedText, operation: `summarize`})
        });

        if(!response.ok){
            throw new Error(`API Error: ${response.status}`);
        }

        const text = await response.text();
        showResult(text);
    }catch (error){
        showResult(`Error: ${error.message}`);
    }


}



async function saveNotes() {
    const notes = document.getElementById('notes').value;

    if (!notes.trim()) {
        alert("Notes cannot be empty.");
        return;
    }

    try {
        const tabs = await chrome.tabs.query({ active: true, currentWindow: true });

        if (tabs.length === 0) {
            alert("No active tab found.");
            return;
        }

        const articleURL = tabs[0].url;

        // Save in local storage
        chrome.storage.local.set({ 'researchNotes': notes }, () => {
            console.log("Note saved locally.");
        });

        
        // Prepare request payload
        const requestData = {
            articleLink: articleURL,
            note: notes
        };

        // Send request to backend
        const response = await fetch("http://localhost:8080/api/saved-notes/add-note", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)
        });


        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Notes saved to backend:", data);
        alert("Notes successfully saved to database.");

    } catch (error) {
        console.error("Error saving notes:", error);
        alert("Failed to save notes to database.");
    }
}


function showResult(content) {
    // document.getElementById('results').innerHTML = 
    // `<div class="result-item">
    //     <div class="result-content">${content}</div>
    // </div>`;

    let resultsContainer = document.getElementById('results')
    console.log(content)

    resultsContainer.style.display = "block"

    resultsContainer.innerText = content;
}


document.addEventListener("DOMContentLoaded", () => {
    const queryInput = document.getElementById("question");
    const goButton = document.getElementById("saveNotesBtn");
    const resultBox = document.getElementById("results");

    goButton.addEventListener("click", async () => {
        const query = queryInput.value.trim();

        if (!query) {
            resultBox.value = "Please enter a query.";
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/research/patient", { // Update with your actual API endpoint
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({content: query, operation: `patient`}),
            });

            if (!response.ok) {
                throw new Error(`HTTP error: Status: ${response.status}`);
            }


            const text = await response.text();
            showResult(text);
        } catch (error) {
            console.error("Error fetching data:", error);
            resultBox.value = "Error fetching response. Please try again.";
        }
    });
});


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


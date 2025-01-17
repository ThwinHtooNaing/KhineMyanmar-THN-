const sidebarDivs = document.querySelectorAll('.sidebar div');
    
        // Function to remove active class from all divs
        function removeActiveClass() {
            sidebarDivs.forEach(div => div.classList.remove('asideActive'));
        }
    
        // Add click event listener to each div
        sidebarDivs.forEach(div => {
            div.addEventListener('click', () => {
                event.preventDefault();
                const path = div.getAttribute('data-path'); // Get the path from the data attribute
                if (path) {
                    window.location.href = path; // Navigate to the path
                }
                removeActiveClass(); // Remove active class from all
                div.classList.add('asideActive'); // Add active class to clicked div
            });
        });
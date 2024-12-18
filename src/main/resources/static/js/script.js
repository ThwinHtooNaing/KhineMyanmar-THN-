// Carousel functionality with infinite looping
const track = document.getElementById("carousel-track");
const slides = Array.from(track.children);
const indicators = document.getElementById("carousel-indicators");

let currentSlide = 0;
const slideCount = slides.length;

// Clone first and last slides for seamless looping
const firstSlideClone = slides[0].cloneNode(true);
const lastSlideClone = slides[slides.length - 1].cloneNode(true);

// Add clones to the track
track.appendChild(firstSlideClone); // Clone of the first slide at the end
track.insertBefore(lastSlideClone, slides[0]); // Clone of the last slide at the beginning

// Update slides array and set initial position
const updatedSlides = Array.from(track.children);
track.style.transform = `translateX(-100%)`; // Start at the first real slide

// Create indicators dynamically
slides.forEach((_, index) => {
    const indicator = document.createElement("div");
    indicator.classList.add("indicator");
    if (index === 0) indicator.classList.add("active");
    indicator.dataset.index = index;
    indicators.appendChild(indicator);
});

const updateIndicators = (index) => {
    const allIndicators = document.querySelectorAll(".indicator");
    allIndicators.forEach((indicator) => indicator.classList.remove("active"));
    if (index >= 0 && index < slideCount) {
        allIndicators[index].classList.add("active");
    }
};

const updateSlide = (index) => {
    // Transition the track to the desired slide
    track.style.transition = "transform 1s ease-in-out";
    track.style.transform = `translateX(-${(index + 1) * 100}%)`;

    // Update indicators
    updateIndicators(index);
    currentSlide = index;
};

// Handle seamless looping using transitionend event
track.addEventListener("transitionend", () => {
    if (currentSlide === slideCount) {
        // If at the clone of the first slide, jump to the actual first slide
        track.style.transition = "none";
        track.style.transform = `translateX(-100%)`;
        currentSlide = 0;
        updateIndicators(currentSlide); // Sync the indicators
    } else if (currentSlide === -1) {
        // If at the clone of the last slide, jump to the actual last slide
        track.style.transition = "none";
        track.style.transform = `translateX(-${slideCount * 100}%)`;
        currentSlide = slideCount - 1;
        updateIndicators(currentSlide); // Sync the indicators
    }
});

// Auto-slide function
const autoSlide = () => {
    currentSlide = (currentSlide + 1) % (slideCount + 1); // Account for the clone
    updateSlide(currentSlide);
};

// Start auto-slide
let slideInterval = setInterval(autoSlide, 3000); // Slides every 3 seconds

// Pause auto-slide on indicator click and navigate to the selected slide
document.querySelectorAll(".indicator").forEach((indicator) => {
    indicator.addEventListener("click", (e) => {
        const index = parseInt(e.target.dataset.index, 10);
        updateSlide(index);
        clearInterval(slideInterval); // Stop auto-slide
        slideInterval = setInterval(autoSlide, 3000); // Restart auto-slide after click
    });
});

  // Select modal elements
  const loginBtn = document.getElementById('login-btn');
  const loginModal = document.getElementById('login-modal');
  const modalOverlay = document.getElementById('modal-overlay');
  const closeModal = document.getElementById('close-modal');

  loginBtn.addEventListener('click', () => {
    loginModal.style.position = 'fixed'; // Fixed positioning relative to viewport
    loginModal.style.top = '60px'; // Position below the navbar (adjust as needed)
    loginModal.style.right = '20px'; // Align to the right corner with padding
    loginModal.style.display = 'block';
    modalOverlay.style.display = 'block';
});




  // Close modal
  closeModal.addEventListener('click', () => {
      loginModal.style.display = 'none';
      modalOverlay.style.display = 'none';
  });

  // Close modal when clicking outside
  modalOverlay.addEventListener('click', () => {
      loginModal.style.display = 'none';
      modalOverlay.style.display = 'none';
  });

  document.querySelectorAll("header nav ul li").forEach((li) => {
    li.addEventListener("click", () => {
        // Remove the 'active' class from all list items
        document.querySelectorAll("header nav ul li").forEach((item) => {
            item.classList.remove("active");
        });

        // Add the 'active' class to the clicked list item
        li.classList.add("active");
    });
});

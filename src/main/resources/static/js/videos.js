let categoryId = null;
let currentPage = 0;

/**
 * 첫 페이지 로드 후 조회수 형식 포멧 적용
 */
function applyViewCountFormat() {
    let viewCountList = document.querySelectorAll(".viewCount");
    viewCountList.forEach(element => {
        let viewCountText = element.textContent;
        let viewCountNumber = parseInt(viewCountText);
        element.textContent = viewCountNumber.toLocaleString();
    })
}

document.addEventListener('DOMContentLoaded', applyViewCountFormat);

/**
 * 조회수 포멧으로 바꾸기
 */
function viewCountFormat(viewCount) {
    let viewCountNumber = parseInt(viewCount);
    return viewCountNumber.toLocaleString();
}

/**
 * 비디오 더 불러오기 버튼
 */
function loadMoreVideo() {
    let searchDate = document.getElementById('searchDate').value;
    let nextPage = currentPage + 1;
    if (categoryId === null) {
        categoryId = '';
    }
    const queryParams = new URLSearchParams({
        searchDate: searchDate,
        categoryId: categoryId,
        page: nextPage
    });

    let url = `/api/videos?${queryParams}`;
    let videoList = document.getElementById('videoList');
    fetch(url)
        .then(response => response.json())
        .then(data => {
            data.content.forEach(video => {
                let videoDiv = document.createElement("div");
                videoDiv.className = "col mb-3";
                let videoUrl = `/videos/${video.id}`;
                videoDiv.innerHTML =
                    '<div class="card shadow-sm">' +
                    '<a href="' + videoUrl + '">' +
                    '<img class="card-img-top" width="100%" height="225" src="' + video.thumbnails + '"/>' +
                    '</a>' +
                    '<div class="card-body">' +
                    '<p class="card-title text-truncate">' + video.title + '</p>' +
                    '<div class="card-text text-truncate">' + video.channelTitle + '</div>' +
                    '<div class="card-text text-truncate">' +
                    '<span>' + formatDate(video.publishedAt) + '</span>' +
                    '<span>' + ' / 조회수 ' + '</span>' +
                    '<span class="viewCount">' + viewCountFormat(video.viewCount) + '</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>'
                ;
                videoList.appendChild(videoDiv);
            })
            if (data.last) {
                loadMoreDiv.style.display = "none";
            }
        });
    currentPage++;
}

// 페이지가 모두 로드 되면 비디오 더불러오기 버튼에 이벤트 추가
function setUpLoadMoreBtnClickEvent() {
    let loadMoreVideoBtn = document.getElementById('loadMoreBtn');
    loadMoreVideoBtn.addEventListener('click', loadMoreVideo)
}

document.addEventListener('DOMContentLoaded', setUpLoadMoreBtnClickEvent);

/**
 * 년-월-일 포멧
 */
function formatDate(inputDateStr) {
    const inputDate = new Date(inputDateStr); // 입력된 문자열을 Date 객체로 파싱

    // 년, 월, 일 값을 가져옴
    const year = inputDate.getFullYear();
    const month = String(inputDate.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 해줌
    const day = String(inputDate.getDate()).padStart(2, '0');

    // yyyy-mm-dd 형태로 조합하여 반환
    return `${year}-${month}-${day}`;
}

function chooseCategory(button) {
    let categoryBtn = document.querySelector('.btn-secondary');
    if (categoryBtn !== null) {
        categoryBtn.classList.remove('btn-secondary');
        categoryBtn.classList.add('btn-primary');
    }

    button.classList.remove('btn-primary');
    button.classList.add('btn-secondary');

    categoryId = button.id;
    currentPage = 0;
    let searchDate = document.getElementById('searchDate').value;
    if (categoryId === 'all') {
        categoryId = '';
    }
    const queryParams = new URLSearchParams({
        searchDate: searchDate,
        categoryId: categoryId,
        page: currentPage
    });

    let videoList = document.getElementById('videoList');
    videoList.innerHTML = '';
    let url = `/api/videos?${queryParams}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            data.content.forEach(video => {
                let videoDiv = document.createElement("div");
                videoDiv.className = "col mb-3";
                let videoUrl = `/videos/${video.id}`;
                videoDiv.innerHTML =
                    '<div class="card shadow-sm">' +
                    '<a href="' + videoUrl + '">' +
                    '<img class="card-img-top" width="100%" height="225" src="' + video.thumbnails + '"/>' +
                    '</a>' +
                    '<div class="card-body">' +
                    '<p class="card-title text-truncate">' + video.title + '</p>' +
                    '<div class="card-text text-truncate">' + video.channelTitle + '</div>' +
                    '<div class="card-text text-truncate">' +
                    '<span>' + formatDate(video.publishedAt) + '</span>' +
                    '<span>' + ' / 조회수 ' + '</span>' +
                    '<span class="viewCount">' + viewCountFormat(video.viewCount) + '</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>'
                ;
                videoList.appendChild(videoDiv);
            })
            if (data.last) {
                loadMoreDiv.style.display = "none";
            } else {
                loadMoreDiv.style.display = "block";
            }
        })
}
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
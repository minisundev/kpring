// 오른쪽 사이드바
export interface SideBarProps {
  close: () => void;
}

// 서버 인포 사이드바
export interface ServerInforProps {
  close: () => void;
  open: boolean;
  serverID: string;
}

// 멤버 프로필 모달 스타일
export const memberProfileModalStyle = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "backround.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

// 멤버 프로필 모달
export interface MemberProfileModalProps {
  openModal: boolean;
  closeModal: () => void;
}

// FIXME :멤버 조회 타입 임의로 지정(이후 API 명세서 수정에 따라 변경 필요) 
export interface Member{

  id: number;
  profilePath: string;
  userName: string;

}
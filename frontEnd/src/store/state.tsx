export interface AppState {
    isLogin: {
        tagNum: number
        nickname: string|null;
        profileImg: string|null;
    }|null;
    isModalOpen: string|null;
}
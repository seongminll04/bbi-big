export const setLogin = (value: 
    {
        tagNum: number;
        nickname: string|null;
        profileImg: string|null;
    } | null) => ({
    type: "SET_LOGIN",
    payload: value,
});
export const setModal = (value:string|null) => ({
    type: "SET_MODAL",
    payload: value,
});
export const updateNickname = (value:string) => ({
    type: "UPDATE_NICKNAME",
    payload: value,
});
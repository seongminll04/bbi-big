export const setLogin = (value: 
    {
        nickname: string|null;
        profileImg: string|null;
    } | null) => ({
    type: "SET_LOGIN",
    payload: value,
});
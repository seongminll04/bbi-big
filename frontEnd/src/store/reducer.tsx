import { AppState } from "./state";

const initialState: AppState = {
  isLogin: null,
  isModalOpen: null
};

const reducer = (
  state: AppState = initialState,
  action: { type: string; payload: any }
) => {
  switch (action.type) {
    case "SET_LOGIN":
      return { ...state, isLogin: action.payload };
    case "UPDATE_NICKNAME":
      return { ...state, isLogin: {
        ...state.isLogin,
        nickname: action.payload
      }};
    case "SET_MODAL":
      return { ...state, isModalOpen: action.payload };
    default:
      return state;
  }
};
export default reducer;
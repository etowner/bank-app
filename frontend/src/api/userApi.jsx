import api from './axiosConfig';

export const getUser = async () => {
  const response = await api.get('/api/v1/user');
  // console.log("getUser response:", response);
  return response.data;
};

export const registerUser = async (username, password) => {
  await api.post(`/api/v1/user/register`, { username, password });
};

export  const loginUser = async (username, password) => {
  await api.post(`/api/v1/user/login`, { username, password }); 
};

export const logoutUser = async() => {
  await api.post('/logout');
};

export const changePassword = async (currentPassword, newPassword) => {
  await api.put(`/api/v1/user/change-password`, { currentPassword, newPassword });
};

export const changeUsername = async (newUsername) => {
  await api.put(`/api/v1/user/change-username`, { newUsername });
};

export const deleteUser = async () => {
  await api.delete('/api/v1/user/close');
};

export const deleteAllAccounts = async () => {
  await api.delete('/api/v1/user/accounts/close-all');
}
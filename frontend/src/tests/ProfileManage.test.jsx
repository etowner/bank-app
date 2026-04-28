import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach, beforeEach, describe, expect, test, vi } from 'vitest';
import ProfileManage from '../components/ProfileManager/ProfileManage';
import { UserContext } from '../UserContext';
import api from '../api/axiosConfig';

vi.mock('../api/axiosConfig', () => ({
  default: {
    post: vi.fn(),
    delete: vi.fn(),
  },
}));

const navigateMock = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');

  return {
    ...actual,
    useNavigate: () => navigateMock,
  };
});

describe('ProfileManage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    navigateMock.mockReset();
  });

  test('opens the profile panel and logs out', async () => {
    const user = userEvent.setup();
    vi.mocked(api.post).mockResolvedValueOnce({});

    render(
      <UserContext.Provider value={{ user: { userID: 'demo' } }}>
        <ProfileManage userID="demo" password="secret" />
      </UserContext.Provider>
    );

    await user.click(screen.getByRole('link', { name: 'demo' }));
    expect(await screen.findByRole('dialog')).toBeInTheDocument();
    expect(screen.getByText('Profile')).toBeInTheDocument();
    expect(screen.getByText(/user: demo secret/i)).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: 'Log Out' }));

    const logoutModalButton = await screen.findByText('Are you sure you want to log out?')
      .then(el => el.closest('.modal-content').querySelector('button.btn-primary'));
    await user.click(logoutModalButton);

    expect(api.post).toHaveBeenCalledWith('/logout');
    expect(navigateMock).toHaveBeenCalledWith('/');
  });

  test('deletes the account and its related data', async () => {
    const user = userEvent.setup();
    vi.mocked(api.delete).mockResolvedValue({});

    render(
      <UserContext.Provider value={{ user: { userID: 'demo' } }}>
        <ProfileManage userID="demo" password="secret" />
      </UserContext.Provider>
    );

    await user.click(screen.getByRole('link', { name: 'demo' }));
    expect(await screen.findByRole('dialog')).toBeInTheDocument();
    await user.click(screen.getByRole('button', { name: 'Delete Account' }));
    await user.click(screen.getByRole('button', { name: /yes/i }));

    expect(api.delete).toHaveBeenCalledWith('/api/v1/user/demo/deleteAll');
    expect(api.delete).toHaveBeenCalledWith('/api/v1/user/demo');
    expect(navigateMock).toHaveBeenCalledWith('/');
  });
});
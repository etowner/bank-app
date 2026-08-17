vi.mock('./context/UserContextProvider', () => ({
  UserContextProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
  useUserContext: () => ({
    user: { username: 'testuser', accounts: [] },
    login: vi.fn(),
    logout: vi.fn(),
    fetchUser: vi.fn(),
  })
}))
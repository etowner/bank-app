import { useUserContext } from './UserContext';
import ProtectedRoute from './ProtectedRoute';
import { render, act, screen, waitFor } from "../lib/test-utils"
import { MemoryRouter, Route, Routes } from 'react-router-dom';

vi.mock('./UserContext', async () => {
    const actual = await vi.importActual('./UserContext');
    return {
        ...actual,
        useUserContext: vi.fn(),
    };
});

afterEach(() => {
  vi.clearAllMocks();
});

describe('useUserContext',  () => {
  
  test('renders outlet when user exists', async () => {
    let resolve: () => void;
    const pending = new Promise<void>(res => { resolve = res; });
  
    const mockUserContext = { 
        username: 'testuser', 
        user: { username: 'testuser', accounts: [], numOfAccounts: 0 }, 
        setUser: vi.fn(), fetchUser: vi.fn().mockReturnValue(pending)
    };
    vi.mocked(useUserContext).mockReturnValue(mockUserContext);
    
    render(
        <MemoryRouter initialEntries={['/home']}>
            <Routes>
                <Route path="/" element={<div data-testid="front-page">Mocked FrontPage</div>} />
                <Route element={<ProtectedRoute />}>
                    <Route path="/home" element={<div data-testid="home">Mocked Home</div>} />
                </Route>
        </Routes>
        </MemoryRouter>
    );
   
    act(() => { resolve!(); }); 
    expect(screen.getByText('Loading...')).toBeInTheDocument();

    await waitFor(() => expect(screen.queryByText('Loading...')).not.toBeInTheDocument());
    expect(screen.getByTestId('home')).toBeInTheDocument();

  });
  test('redirects to / when user is null', async () => {
    let resolve: () => void;
    const pending = new Promise<void>(res => { resolve = res; });
    const mockUserContext = { 
        username: null, 
        user: null, 
        setUser: vi.fn(), fetchUser: vi.fn().mockReturnValue(pending) 
    };
    
    vi.mocked(useUserContext).mockReturnValue(mockUserContext);
   
    render(
        <MemoryRouter initialEntries={['/home']}>
            <Routes>
                <Route path="/" element={<div>Mocked FrontPage</div>} />
                <Route element={<ProtectedRoute />}>
                    <Route path="/home" element={<div data-testid="home">Mocked Home</div>} />
                </Route>
            </Routes>
        </MemoryRouter>
    );

    act(() => { resolve!(); });
    // screen.debug();
    expect(screen.getByText('Loading...')).toBeInTheDocument();
    screen.debug();
    await waitFor(() => expect(screen.queryByText('Loading...')).not.toBeInTheDocument());
    
    screen.debug();
    expect(screen.getByText('Mocked FrontPage')).toBeInTheDocument();
  });
});
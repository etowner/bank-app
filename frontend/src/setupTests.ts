import '@testing-library/jest-dom/vitest';
import { setupServer } from 'msw/node'
import { restHandlers } from './lib/handler'


const server  = setupServer(...restHandlers)

// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))

// Close server after all tests
afterAll(() => server.close())

// Reset handlers after each test for test isolation
afterEach(() => server.resetHandlers())

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query: string) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    // addEventListener: vi.fn(),
    // removeEventListener: vi.fn(),
    // dispatchEvent: vi.fn(),
  })),
});
import { render, screen } from "@testing-library/react";
import FrontPage from "./FrontPage";

// Mock the AccountBox component to isolate the FrontPage component during testing
vi.mock('./AccountBox', () => ({
  default: () => <div data-testid="account-box">Mocked AccountBox</div>,
}));

test('render with context', () => {
  render(<FrontPage />);
})

describe("FrontPage", () => {
  beforeEach(() => {
    render(<FrontPage />);
  });

  test('renders the bank application heading', () => {
    expect(
      screen.getByRole('heading', { name: /bank application/i })
    ).toBeInTheDocument();
  });

  test('renders the AccountBox', () => {
    expect(screen.getByTestId('account-box')).toBeInTheDocument();
  });
});

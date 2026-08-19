import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { useUserContext } from "./UserContext";
import { UserContextProvider } from "./UserContextProvider";

const TestConsumer = () => {
  const { user } = useUserContext()
  return <div>{user ? user.username : 'no user'}</div>
}

test('provides null user initially', () => {
  render(
    <MemoryRouter>
      <UserContextProvider>
        <TestConsumer />
      </UserContextProvider>
    </MemoryRouter>
  )
  expect(screen.getByText('no user')).toBeInTheDocument()
})
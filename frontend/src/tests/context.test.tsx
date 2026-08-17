import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { useUserContext } from "../context/UserContext";
import { UserContextProvider } from "../context/UserContextProvider";

// const userContext = vi.fn()


// function fetchUser(id) {
//   return Promise.resolve({ id, name: 'Alice' })
// }

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

function getUser() {
    return Promise.resolve({username: 'username', accounts: []})
}

function getInvalidUser() {
    return Promise.resolve({user: null, error :'User not found'})
}

// const user = 

test('successful fetch', async () => {
    await getUser()
   const { user } = useUserContext()
   assert(user?.username)
   expect(user.username).toBe('username')
   expect(user.accounts).toBe([])
})

test('failed fetch', async () => {
    await getInvalidUser()
    const { user, error } = useUserContext()
    expect(user).toBe(null)
    expect(error).toBe('User not found')
})
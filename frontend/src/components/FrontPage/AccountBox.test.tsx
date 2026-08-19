import { render, screen } from "@testing-library/react";
import AccountBox from "./AccountBox";
import FrontPage from "./FrontPage"
import { useUserContext } from "../../context/UserContext";
import { Tab, Tabs} from "react-bootstrap";


describe("AccountBox", () => {
  beforeEach(() =>{
    render(<AccountBox/>)
  })

  test('renders create account tab', () => {
    expect(
     screen.getByRole('tab', { name: /Create Account/i })
    ).toBeInTheDocument()
  })

  test('changing tabs to login works', () => {
    const handleTabSwitch = vi.fn()
    const activeKey = "create"
    const newKey = "log"
    // const activeTab = screen.getByRole('tab', { name: /Create Account/i})

    render(<Tabs activeKey={activeKey} onSelect={handleTabSwitch}/>)
    screen.getByRole('tab', { name: /Create Account/i }).click()
   
    expect(handleTabSwitch).toHaveBeenCalledWith(newKey)

    expect(
     screen.getByRole('tab', { name: /Log In/i })
    ).toBeInTheDocument()
    
  })




});

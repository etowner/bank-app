 
import  { useEffect, useState, use } from "react";
import { Accordion, useAccordionButton } from "react-bootstrap";
import { Button, Card, Col, Container, Nav, Navbar, Row, Table, } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import "../../styles/Home.css";
import Deposit from "./Deposit";
import Withdraw from "./Withdraw";
import CloseAccount from "./CloseAccount";
import LineChart from "./LineChart";
import { UserContext } from "../../UserContext";
import { getTransactions } from "../../api/transactionApi";
import { getAccount } from "../../api/accountApi";

function CustomToggle({ children, eventKey }) {
  const showAction = useAccordionButton(eventKey, () => {});

  return (
    <Button variant="dark" onClick={showAction} className="mb-3">
      {" "}
      {children}
    </Button>
  );
}

const Account = () => {
  const { username } = use(UserContext);
  const { accountNumber } = useParams();
  const [account, setAccount] = useState(null);
  const [transactions, setTransactions] = useState([]);

  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/home`);
  };

  const fetchAccountData = async () => {
    try {
      const account = await getAccount(accountNumber);
      setAccount(account);
      console.log("Fetched account data:", account);
    } catch (error) {
      console.error("Error fetching account data:", error);
    }

    try {
      const transactions = await getTransactions(accountNumber);
      setTransactions(transactions);
      console.log("Fetched transactions:", transactions);
    } catch (error) {
      console.error("Error fetching transactions:", error);
    }
  };

  // const fetchAccount = async () => {
  //   try {
  //     const account = await getAccount(accountNumber);
  //     setAccount(account);
  //     console.log("Fetched account data:", account);
  //   } catch (error) {
  //     console.error("Error fetching account data:", error);
  //   }
    
  // };

  // const fetchTransactions = async () => {
  //   try {
  //     const transactions = await getTransactions(accountNumber);
      
  //     setTransactions(transactions);
  //     console.log("Fetched transactions:", transactions);
        
  //     } catch (error) {
  //       console.error("Error fetching transactions:", error);
  //     }
  // };

  // Fetch account data
  useEffect(() => {
    fetchAccountData();
    //fetchAccount();
  }, [accountNumber, username, account?.balance]);

  // useEffect(() => {
  //   if (account) {
  //     fetchTransactions();
  //   }
  // }, [account.balance]);

  return (
    <div className="Home">
      <Navbar bg="dark" variant="dark" className="mb-5">
        <Container>
          <Navbar.Collapse>
            <Nav className="me-auto">
              <Nav.Link onClick={handleClick}>Back</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container>
        <Row className="mb-3"></Row>
        {/* Account Info */}
        <Row className="mb-3">
          <h1>
            {account?.type} - {accountNumber}
          </h1>
        </Row>
        <Row className="mb-3">
          <h3>Balance: ${account?.balance} </h3>
        </Row>

        <Row className="mb-4 justify-content-md-center">
          <Col sm={6}>
            <Card border="dark" style={{ width: "32rem" }}>
              <Card.Header
                style={{ color: "white", backgroundColor: "#282c34" }}
              >
                <h3>Transaction History</h3>
              </Card.Header>
              <Card.Body>
                <Table bordered hover>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Type</th>
                      <th>Amount</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions &&
                      Object.entries(transactions).map(([key, value]) => (
                        <tr key={key}>
                          <td>{key}</td>
                          <td>
                            {value.type} {value.counterparty}
                          </td>
                          <td>{value.amount}</td>
                          <td>{value.timestamp}</td>
                        </tr>
                      ))}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
          {/* Account Actions */}
          <Col sm={5}>
            <Card border="dark" style={{ width: "30rem" }}>
              <Card.Header
                style={{ color: "white", backgroundColor: "#282c34" }}
              >
                <h3>Transaction Options</h3>
              </Card.Header>
              <Card.Body>
                <Accordion defaultActiveKey="2">
                  <CustomToggle eventKey="0">Deposit</CustomToggle>
                  <Col>
                    <Accordion.Collapse eventKey="0">
                      <Card.Body>
                        <Deposit setAccount={setAccount} />
                      </Card.Body>
                    </Accordion.Collapse>
                  </Col>
                  <Col>
                    <CustomToggle eventKey="1">Withdraw</CustomToggle>
                    <Accordion.Collapse eventKey="1">
                      <Card.Body>
                        <Withdraw
                          balance={account?.balance}
                          setAccount={setAccount}
                        />
                      </Card.Body>
                    </Accordion.Collapse>
                  </Col>
                </Accordion>
              </Card.Body>
            </Card>
          </Col>
        </Row>
        <Row className="mb-3">
          <Card>
            <LineChart
              accountNumber={accountNumber}
              transHistory={transactions}
            />
          </Card>
        </Row>
        <Row className="md-5 justify-content-md-center">
          <Col>
            <CloseAccount />
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Account;

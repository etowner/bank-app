
import { useEffect, useCallback, useState } from "react";
import { Accordion, useAccordionButton } from "react-bootstrap";
import { Button, Card, Col, Container, Nav, Navbar, Row, Table, } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import "../../styles/Home.css";
import Deposit from "./Deposit";
import Withdraw from "./Withdraw";
import CloseAccount from "./CloseAccount";
import LineChart from "./LineChart";
import { getTransactions } from "../../api/transactionApi";
import { getAccount } from "../../api/accountApi";
import { formatDate } from '../../lib/utils';
import type { Account, Transaction } from "../../lib/types";
import { getAxiosError } from "../../api/axiosConfig";

function CustomToggle({ children, eventKey }: { children: React.ReactNode; eventKey: string }) {
  const showAction = useAccordionButton(eventKey, () => { /* empty */ });

  return (
    <Button variant="dark" onClick={showAction} className="mb-3">
      {" "}
      {children}
    </Button>
  );
}


const AccountPage = () => {
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const [account, setAccount] = useState<Account | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleClick = () => {
    void navigate(`/home`);
  };

  const fetchAccountData = useCallback(async () => {
    // Fetch account and transactions in parallel, so they will succeed together or fail together
    try {
        const [acc, txns] = await Promise.all([ 
            getAccount(accountNumber!),
            getTransactions(accountNumber!)
        ]);
        setAccount(acc);
        setTransactions(txns);
    } catch (err) {
        setError(getAxiosError(err));
        console.error(err);
    }
   
  }, [accountNumber]);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    void fetchAccountData();
  }, [fetchAccountData]);


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
                      <th>Type</th>
                      <th>Amount</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions.map((transaction) => (
                      <tr key={transaction.id}>
                        <td>{transaction.type} {transaction.counterparty}</td>
                        <td>{transaction.amount}</td>
                        <td>{formatDate(transaction.timestamp)}</td>
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
                        <Deposit setAccount={setAccount} fetchAccountData={fetchAccountData} />
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
                          fetchAccountData={fetchAccountData}
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
              accountNumber={accountNumber!}
              transactions={transactions}
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

export default AccountPage;

/* eslint-disable react-hooks/set-state-in-effect */
import { use, useEffect, useState } from "react";
import { Card, Col, Container, Row, Alert } from "react-bootstrap";
import Transfer from "./Transfer";
import AccList from "./AccList";
import Header from "./Header";
import "../../styles/Home.css";
import OpenAccount from "./OpenAccount";
import PieChart from "./PieChart";
import { UserContext } from "../../UserContext";
import { createAccount } from "../../api/accountApi";

const Home = () => {
  const { user, username, fetchUser } = use(UserContext)
  const [accList, setAccList] = useState([]);
  const [error, setError] = useState();

  const openAcc = async (type) => {
    if( accList.length >= 3){
      setError("You can only have 3 accounts");
      return;
    }
    try {
      // eslint-disable-next-line no-unused-vars
      const newAccount = createAccount(type);
      // console.log("Opened account:", newAccount);
    } catch (error) {
      console.error(error);
    }

    fetchUser(); 
  };

  useEffect(() => {
    fetchUser();
    console.log("User data:", user);
  }, []);
  
  useEffect(() => {
    if (user) {
      setAccList(user.accounts ?? []);
    }
  }, [user.accounts]);

  return (
    <div className="Home">
      <Header username={username} />
      <Container>
        <Row className="mb-5">
          <h2>Welcome {username}</h2>
        </Row>
        <Row xs={1} md={2} lg={2} className="g-4">
          {/* Lists Accounts and provides open account options */}
          <Col key={1}>
            <Card border="dark">
              <AccList username={username} accList={accList} />
              <OpenAccount username={username} openAcc={openAcc} setError={setError} />
              {error && <Alert className="" variant="danger">{error}</Alert>}
            </Card>
          </Col>
          <Col key={2}>
            <Transfer />
          </Col>
          <Col key={3}>
            <Card style={{ width: "32rem" }} className="mb-2">
              <PieChart accounts={accList} />
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
